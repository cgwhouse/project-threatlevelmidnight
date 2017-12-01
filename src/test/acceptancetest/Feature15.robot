| *Setting* | *Value*            |
| Library   | keywords.Feature15 |

| *Test Case*                      | *Action*                         | *Argument*  | *Argument* | *Argument* | *Argument* | *Argument* |
| Test Sum Numbers Multiple Values | Start Sum Numbers With Arguments | 6           | 3          | 7          | 12         | 8          |
|                                  | ${numbers}=                      | Get Numbers |            |            |            |            |
|                                  | Should Contain                   | ${numbers}  | 6          |            |            |            |
|                                  | Should Contain                   | ${numbers}  | 3          |            |            |            |
|                                  | Should Contain                   | ${numbers}  | 7          |            |            |            |
|                                  | Should Contain                   | ${numbers}  | 12         |            |            |            |
|                                  | Should Contain                   | ${numbers}  | 8          |            |            |            |
