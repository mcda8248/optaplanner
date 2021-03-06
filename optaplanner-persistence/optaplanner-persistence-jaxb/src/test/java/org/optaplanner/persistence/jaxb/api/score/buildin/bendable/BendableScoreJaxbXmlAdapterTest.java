/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.persistence.jaxb.api.score.buildin.bendable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.persistence.jaxb.api.score.AbstractScoreJaxbXmlAdapterTest;

import static org.junit.Assert.*;

public class BendableScoreJaxbXmlAdapterTest extends AbstractScoreJaxbXmlAdapterTest {

    @Test
    public void serializeAndDeserialize() {
        assertSerializeAndDeserialize(null, new TestBendableScoreWrapper(null));
        BendableScore score = BendableScore.valueOfInitialized(new int[]{1000, 200}, new int[]{34});
        assertSerializeAndDeserialize(score, new TestBendableScoreWrapper(score));
        score = BendableScore.valueOf(-7, new int[]{1000, 200}, new int[]{34});
        assertSerializeAndDeserialize(score, new TestBendableScoreWrapper(score));
    }

    @XmlRootElement
    public static class TestBendableScoreWrapper extends TestScoreWrapper<BendableScore> {

        @XmlJavaTypeAdapter(BendableScoreJaxbXmlAdapter.class)
        private BendableScore score;

        @SuppressWarnings("unused")
        private TestBendableScoreWrapper() {
        }

        public TestBendableScoreWrapper(BendableScore score) {
            this.score = score;
        }

        @Override
        public BendableScore getScore() {
            return score;
        }

    }

}
