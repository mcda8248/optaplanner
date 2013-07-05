************************************************************************
file with basedata            : mm63_.bas
initial value random generator: 334401647
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  12
horizon                       :  85
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     10      0       14        2       14
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          3           5   7   8
   3        3          1           5
   4        3          2           9  10
   5        3          1           6
   6        3          2           9  11
   7        3          1          11
   8        3          2          10  11
   9        3          1          12
  10        3          1          12
  11        3          1          12
  12        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     5       9    6   10    2
         2     9       9    4   10    2
         3    10       7    3    9    1
  3      1     3       4    2    2    4
         2     4       4    2    1    2
         3     8       4    2    1    1
  4      1     1       5    7    5    3
         2     2       4    5    4    2
         3     5       3    4    3    2
  5      1     2       7    6    2    4
         2     4       5    5    2    3
         3     5       4    3    1    3
  6      1     1       3   10    6   10
         2     3       3   10    4    5
         3     9       3   10    2    3
  7      1     1       8    7    7    7
         2     7       6    7    7    6
         3    10       4    6    4    5
  8      1     4       2   10    9    5
         2     6       2    9    9    4
         3     9       1    9    8    4
  9      1     1       5    9    9    5
         2     4       5    5    8    4
         3     9       4    1    8    4
 10      1     3       9    9    9    8
         2     3      10    9    8    9
         3    10       7    8    8    8
 11      1     5       9    5    6    9
         2    10       4    5    2    6
         3    10       6    4    4    7
 12      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   19   23   59   52
************************************************************************