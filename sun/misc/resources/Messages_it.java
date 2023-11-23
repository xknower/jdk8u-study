package sun.misc.resources;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for sun.misc.
 *
 * @author Michael Colburn
 */

public final class Messages_it extends java.util.ListResourceBundle {

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     * <p>
     * @return the contents of this <code>ResourceBundle</code>.
     */
    protected Object[][] getContents() {
        return new Object[][]{
            {"optpkg.versionerror", "ERRORE: Formato versione non valido nel file JAR {0}. Verificare nella documentazione il formato della versione supportato."},
            {"optpkg.attributeerror", "ERRORE: L''attributo manifest JAR {0} richiesto non \u00E8 impostato nel file JAR {1}."},
            {"optpkg.attributeserror", "ERRORE: Alcuni attributi manifesti JAR obbligatori non sono impostati nel file JAR {0}."}
        };
    }

}
