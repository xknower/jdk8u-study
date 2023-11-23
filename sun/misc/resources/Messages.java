package sun.misc.resources;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for sun.misc.
 *
 * @author Michael Colburn
 */

public final class Messages extends java.util.ListResourceBundle {

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     * <p>
     * @return the contents of this <code>ResourceBundle</code>.
     */
    protected Object[][] getContents() {
        return new Object[][]{
            {"optpkg.versionerror", "ERROR: Invalid version format used in {0} JAR file. Check the documentation for the supported version format."},
            {"optpkg.attributeerror", "ERROR: The required {0} JAR manifest attribute is not set in {1} JAR file."},
            {"optpkg.attributeserror", "ERROR: Some required JAR manifest attributes are not set in {0} JAR file."}
        };
    }

}
