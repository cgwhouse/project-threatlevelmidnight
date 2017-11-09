| *Setting* | *Value*            |
| Library   | keywords.Feature06 |

| *Variable*  | *Value*                                                                                                                                                                                                                          |
| ${expected} | usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\n   length the length of the box (float)\n   width the width of the box (float)\n   height the height of the box (float) |

| *Test Case*        | *Action*                     | *Argument*         | *Argument*  | *Argument*  | *Argument*  |
| Test Help Flag     | Start Program With Arguments | 7                  | --help      | 3           | 2           |
|                    | ${output}=                   | Get Program Output |             |             |             |
|                    | Should Be Equal              | ${expected}        | ${output}   |             |             |
