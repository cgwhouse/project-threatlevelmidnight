| *Setting* | *Value*                                                      |
| Library                                                                  | keywords.Feature01 |

| *Variable*                                                               | *Value*                                |
| ${expected} | usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: unrecognized arguments: 43 |

| *Test Case*                                                              | *Action*                               | *Argument*                       | *Argument*        | *Argument* | *Argument* |
| Test Volume Calculator Normal Function | Start Program With Arguments    | 7                                      | 5                                | 2                 |                              |
|                                                                          | ${length}=                             | keywords.Feature01.Get Length    |                   |                              |        |
|                                                                          | Should Be Equal                        | 7                                | ${length}         |                              |        |
|                                                                          | ${width}=                              | Get Width                        |                   |                              |        |
|                                                                          | Should Be Equal                        | 5                                | ${width}          |                              |        |
|                                                                          | ${height}=                             | Get Height                       |                   |                              |        |
|                                                                          | Should Be Equal                        | 2                                | ${height}         |                              |        |
|                                                                          | ${output}=                             | Get Program Output |             |                   |                              |
|                                                                          | Should Be Equal                        | 70                               | ${output}         |                              |        |
| Test Unrecognized Argument                                               | Start Program With Arguments           | 7                                | 5                 | 2                            | 43     |
|                                                                          | ${output}=                             | Get Program Output |             |                   |                              |
|                                                                          | Should Be Equal                        | ${expected}                      | ${output}         |                              |        |
| Test Absurd Program Normal Function                                      | Start Absurd Program With Arguments    | dog                              | 2                 | true                         | 3.5    |
|                                                                          | ${pet}=                                | Get Pet                          |                   |                              |        |
|                                                                          | Should Be Equal                        | dog                              | ${pet}            |                              |        |
|                                                                          | ${number}=                             | Get Number                       |                   |                              |        |
|                                                                          | Should Be Equal                        | 2                                | ${number}         |                              |        |
|                                                                          | ${rainy}=                              | Get Rainy                        |                   |                              |        |
|                                                                          | Should Be Equal                        | true                             | ${rainy}          |                              |        |
|                                                                          | ${bathrooms}=                          | Get Bathrooms                    |                   |                              |        |
|                                                                          | Should Be Equal                        | 3.5                              | ${bathrooms} |    |                              |
