| *Setting* | *Value*            |
| Library   | keywords.Feature03 |

| *Variable*  | *Value*                                                                                                                       |
| ${expected} | usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument width: invalid float value: something |

| *Test Case*                     | *Action*                     | *Argument*         | *Argument*  | *Argument*  |
| Test Incorrect Datatype Message | Start Program With Arguments | 7                  | something   | 2           |
|                                 | ${output}=                   | Get Program Output |             |             |
|                                 | Should Be Equal              | ${output}          | ${expected} |             |