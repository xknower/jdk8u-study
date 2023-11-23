package sun.text.resources.en;

import sun.util.resources.ParallelListResourceBundle;

public class FormatData_en_SG extends ParallelListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "NumberPatterns",
                new String[] {
                    "#,##0.###",
                    "\u00a4#,##0.00",
                    "#,##0%",
                }
            },
            { "NumberElements",
                new String[] {
                    ".",
                    ",",
                    ";",
                    "%",
                    "0",
                    "#",
                    "-",
                    "E",
                    "\u2030",
                    "\u221e",
                    "NaN",
                }
            },
            { "DatePatterns",
                new String[] {
                    "EEEE, d MMMM, yyyy", // full date pattern
                    "d MMMM, yyyy",       // long date pattern
                    "d MMM, yyyy",        // medium date pattern
                    "d/M/yy",             // short date pattern
                }
            },
        };
    }
}