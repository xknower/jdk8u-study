package sun.text.resources.pl;

import sun.util.resources.ParallelListResourceBundle;

public class FormatData_pl_PL extends ParallelListResourceBundle {
    /**
     * Overrides ParallelListResourceBundle
     */
    protected final Object[][] getContents() {
        return new Object[][] {
            { "NumberPatterns",
                new String[] {
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "#,##0.## \u00A4;-#,##0.## \u00A4", // currency pattern
                    "#,##0%" // percent pattern
                }
            },
            { "TimePatterns",
                new String[] {
                    "HH:mm:ss z", // full time pattern
                    "HH:mm:ss z", // long time pattern
                    "HH:mm:ss", // medium time pattern
                    "HH:mm", // short time pattern
                }
            },
            { "DatePatterns",
                new String[] {
                    "EEEE, d MMMM yyyy", // full date pattern
                    "d MMMM yyyy", // long date pattern
                    "yyyy-MM-dd", // medium date pattern
                    "dd.MM.yy", // short date pattern
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "{1} {0}" // date-time pattern
                }
            }
        };
    }
}