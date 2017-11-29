| *Setting* | *Value*          |
| Library                      | keywords.Feature07 |

| *Test Case*                  | *Action*                        | *Argument*                       | *Argument*    | *Argument* | *Argument* | *Argument* | *Argument* | *Argument* |
| Test Short Form Arguments    | Start Program With Arguments    | -t                               | ellipsoid     | 7                                                                   | 3    | -d    | 1    | 2    |
|                              | ${length}=                      | keywords.Feature07.Get Length    |               |                                                                     |      |       |      |      |
|                              | Should Be Equal                 | 7                                | ${length}     |                                                                     |      |       |      |      |
|                              | ${width}=                       | Get Width                        |               |                                                                     |      |       |      |      |
|                              | Should Be Equal                 | 3                                | ${width}      |                                                                     |      |       |      |      |
|                              | ${height}=                      | Get Height                       |               |                                                                     |      |       |      |      |
|                              | Should Be Equal                 | 2                                | ${height}     |                                                                     |      |       |      |      |
|                              | ${type}=                        | Get Type                         |               |                                                                     |      |       |      |      |
|                              | Should Be Equal                 | ellipsoid                        | ${type}       |                                                                     |      |       |      |      |
|                              | ${digits}=                      | Get Digits                       |               |                                                                     |      |       |      |      |
|                              | Should Be Equal                 | 1                                | ${digits}     |                                                                     |      |       |      |      |
