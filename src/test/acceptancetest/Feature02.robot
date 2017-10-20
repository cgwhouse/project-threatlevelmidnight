| *Setting* | *Value*            |
| Library   | keywords.Feature02 |

| *Variable*  | *Value*                                                                                                                                                                                                                         |
| ${expected} | usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\n   length the length of the box (float)\n   width the width of the box (float)\n   height the height of the box (float) |

| *Test Case*        | *Action*                     | *Argument*         | *Argument*  |
| Test Usage Message | Start Program With Arguments | -h                 |             |
|                    | ${output}=                   | Get Program Output |             |
|                    | Should Be Equal              | ${expected}        | ${output}   |
