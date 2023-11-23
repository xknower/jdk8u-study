package sun.text.resources.ca;

import sun.util.resources.ParallelListResourceBundle;

public class FormatData_ca_ES extends ParallelListResourceBundle {
    /**
     * Overrides ParallelListResourceBundle
     */
    protected final Object[][] getContents() {
        return new Object[][] {
            { "NumberPatterns",
                new String[] {
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "\u00A4 #,##0;-\u00A4 #,##0", // currency pattern
                    "#,##0%" // percent pattern
                }
            },
        };
    }
}