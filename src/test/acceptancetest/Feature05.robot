| *Setting* | *Value*            |
| Library   | keywords.Feature05 |

| *Test Case*                  | *Action*                       | *Argument*         | *Argument*   | *Argument* | *Argument* | *Argument* | *Argument* | *Argument* |
| Test Named Argument Anywhere | Start Program With Arguments   | --type             | ellipsoid    | 7          | 3          | --digits   | 1          | 2          |
|                              | ${length}=                     | Get Length         |              |            |            |            |            |            |
|                              | Should Be Equal                | 7                  | ${length}    |            |            |            |            |            |
|                              | ${width}=                      | Get Width          |              |            |            |            |            |            |
|                              | Should Be Equal                | 3                  | ${width}     |            |            |            |            |            |
|                              | ${height}=                     | Get Height         |              |            |            |            |            |            |
|                              | Should Be Equal                | 2                  | ${height}    |            |            |            |            |            |
|                              | ${type}=                       | Get Type           |              |            |            |            |            |            |
|                              | Should Be Equal                | ellipsoid          | ${type}      |            |            |            |            |            |
|                              | ${digits}=                     | Get Digits         |              |            |            |            |            |            |
|                              | Should Be Equal                | 1                  | ${digits}    |            |            |            |            |            |
