/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.solver;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.optaplanner.core.impl.phase.scope.AbstractPhaseScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.AbstractTermination;
import org.optaplanner.core.impl.solver.termination.Termination;

/**
 * Concurrency notes:
 * Condition predicate on ({@link #problemFactChangeQueue} is not empty or {@link #terminatedEarly} is true).
 */
public class BasicPlumbingTermination extends AbstractTermination {

    protected final boolean daemon;

    protected boolean terminatedEarly = false;
    protected BlockingQueue<ProblemFactChange> problemFactChangeQueue = new LinkedBlockingQueue<>();

    protected boolean problemFactChangesBeingProcessed = false;

    public BasicPlumbingTermination(boolean daemon) {
        this.daemon = daemon;
    }

    // ************************************************************************
    // Plumbing worker methods
    // ************************************************************************

    public synchronized void resetTerminateEarly() {
        terminatedEarly = false;
    }

    /**
     * Concurrency note: unblocks {@link #waitForRestartSolverDecision()}.
     * @return true if successful
     */
    public synchronized boolean terminateEarly() {
        boolean terminationEarlySuccessful = !terminatedEarly;
        if (terminationEarlySuccessful) {
            logger.info("Terminating solver early.");
        }
        terminatedEarly = true;
        notifyAll();
        return terminationEarlySuccessful;
    }

    public synchronized boolean isTerminateEarly() {
        return terminatedEarly;
    }

    /**
     * If this returns true, then the problemFactChangeQueue is definitely not empty.
     * <p>
     * Concurrency note: Blocks until {@link #problemFactChangeQueue} is not empty or {@link #terminatedEarly} is true.
     * @return true if the solver needs to be restarted
     */
    public synchronized boolean waitForRestartSolverDecision() {
        if (!daemon) {
            return !problemFactChangeQueue.isEmpty() && !terminatedEarly;
        } else {
            while (problemFactChangeQueue.isEmpty() && !terminatedEarly) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Solver thread was interrupted during Object.wait().", e);
                }
            }
            return !terminatedEarly;
        }
    }

    /**
     * Concurrency note: unblocks {@link #waitForRestartSolverDecision()}.
     * @param problemFactChange never null
     * @return as specified by {@link Collection#add}
     */
    public synchronized boolean addProblemFactChange(ProblemFactChange problemFactChange) {
        boolean added = problemFactChangeQueue.add(problemFactChange);
        notifyAll();
        return added;
    }

    public synchronized BlockingQueue<ProblemFactChange> startProblemFactChangesProcessing() {
        problemFactChangesBeingProcessed = true;
        return problemFactChangeQueue;
    }

    public synchronized void endProblemFactChangesProcessing() {
        problemFactChangesBeingProcessed = false;
    }

    public synchronized boolean isEveryProblemFactChangeProcessed() {
        return problemFactChangeQueue.isEmpty() && !problemFactChangesBeingProcessed;
    }

    // ************************************************************************
    // Termination worker methods
    // ************************************************************************

    @Override
    public synchronized boolean isSolverTerminated(DefaultSolverScope solverScope) {
        // Destroying a thread pool with solver threads will only cause it to interrupt those solver threads
        if (Thread.currentThread().isInterrupted()) { // Does not clear the interrupted flag
            logger.info("The solver thread got interrupted, so this solver is terminating early.");
            terminatedEarly = true;
        }
        return terminatedEarly || !problemFactChangeQueue.isEmpty();
    }

    @Override
    public boolean isPhaseTerminated(AbstractPhaseScope phaseScope) {
        throw new IllegalStateException(
                "BasicPlumbingTermination configured only as solver termination."
                + " It is always bridged to phase termination.");
    }

    @Override
    public double calculateSolverTimeGradient(DefaultSolverScope solverScope) {
        return -1.0; // Not supported
    }

    @Override
    public double calculatePhaseTimeGradient(AbstractPhaseScope phaseScope) {
        throw new IllegalStateException(
                "BasicPlumbingTermination configured only as solver termination."
                + " It is always bridged to phase termination.");
    }

    // ************************************************************************
    // Other methods
    // ************************************************************************

    @Override
    public Termination createChildThreadTermination(DefaultSolverScope solverScope, ChildThreadType childThreadType) {
        return this;
    }

    @Override
    public String toString() {
        return "BasicPlumbing()";
    }

}
