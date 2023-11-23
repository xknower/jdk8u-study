package sun.misc.resources;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for sun.misc.
 *
 * @author Michael Colburn
 */

public final class Messages_sv extends java.util.ListResourceBundle {

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     * <p>
     * @return the contents of this <code>ResourceBundle</code>.
     */
    protected Object[][] getContents() {
        return new Object[][]{
            {"optpkg.versionerror", "FEL: Ogiltigt versionsformat i {0} JAR-fil. Kontrollera i dokumentationen vilket versionsformat som st\u00F6ds."},
            {"optpkg.attributeerror", "FEL: Obligatoriskt JAR manifest-attribut {0} \u00E4r inte inst\u00E4llt i {1} JAR-filen."},
            {"optpkg.attributeserror", "FEL: Vissa obligatoriska JAR manifest-attribut \u00E4r inte inst\u00E4llda i {0} JAR-filen."}
        };
    }

}
