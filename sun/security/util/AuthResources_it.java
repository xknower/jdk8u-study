package sun.security.util;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for the following packages:
 *
 * <ol>
 * <li> com.sun.security.auth
 * <li> com.sun.security.auth.login
 * </ol>
 *
 */
public class AuthResources_it extends java.util.ListResourceBundle {

    private static final Object[][] contents = {

        // NT principals
        {"invalid.null.input.value", "input nullo non valido: {0}"},
        {"NTDomainPrincipal.name", "NTDomainPrincipal: {0}"},
        {"NTNumericCredential.name", "NTNumericCredential: {0}"},
        {"Invalid.NTSid.value", "Valore NTSid non valido"},
        {"NTSid.name", "NTSid: {0}"},
        {"NTSidDomainPrincipal.name", "NTSidDomainPrincipal: {0}"},
        {"NTSidGroupPrincipal.name", "NTSidGroupPrincipal: {0}"},
        {"NTSidPrimaryGroupPrincipal.name", "NTSidPrimaryGroupPrincipal: {0}"},
        {"NTSidUserPrincipal.name", "NTSidUserPrincipal: {0}"},
        {"NTUserPrincipal.name", "NTUserPrincipal: {0}"},

        // UnixPrincipals
        {"UnixNumericGroupPrincipal.Primary.Group.name",
                "UnixNumericGroupPrincipal [gruppo primario]: {0}"},
        {"UnixNumericGroupPrincipal.Supplementary.Group.name",
                "UnixNumericGroupPrincipal [gruppo supplementare]: {0}"},
        {"UnixNumericUserPrincipal.name", "UnixNumericUserPrincipal: {0}"},
        {"UnixPrincipal.name", "UnixPrincipal: {0}"},

        // com.sun.security.auth.login.ConfigFile
        {"Unable.to.properly.expand.config", "Impossibile espandere correttamente {0}"},
        {"extra.config.No.such.file.or.directory.",
                "{0} (file o directory inesistente)"},
        {"Configuration.Error.No.such.file.or.directory",
                "Errore di configurazione:\n\tFile o directory inesistente"},
        {"Configuration.Error.Invalid.control.flag.flag",
                "Errore di configurazione:\n\tflag di controllo non valido, {0}"},
        {"Configuration.Error.Can.not.specify.multiple.entries.for.appName",
            "Errore di configurazione:\n\timpossibile specificare pi\u00F9 valori per {0}"},
        {"Configuration.Error.expected.expect.read.end.of.file.",
                "Errore di configurazione:\n\tprevisto [{0}], letto [end of file]"},
        {"Configuration.Error.Line.line.expected.expect.found.value.",
            "Errore di configurazione:\n\triga {0}: previsto [{1}], trovato [{2}]"},
        {"Configuration.Error.Line.line.expected.expect.",
            "Errore di configurazione:\n\triga {0}: previsto [{1}]"},
        {"Configuration.Error.Line.line.system.property.value.expanded.to.empty.value",
            "Errore di configurazione:\n\triga {0}: propriet\u00E0 di sistema [{1}] espansa a valore vuoto"},

        // com.sun.security.auth.module.JndiLoginModule
        {"username.","Nome utente: "},
        {"password.","Password: "},

        // com.sun.security.auth.module.KeyStoreLoginModule
        {"Please.enter.keystore.information",
                "Immettere le informazioni per il keystore"},
        {"Keystore.alias.","Alias keystore: "},
        {"Keystore.password.","Password keystore: "},
        {"Private.key.password.optional.",
            "Password chiave privata (opzionale): "},

        // com.sun.security.auth.module.Krb5LoginModule
        {"Kerberos.username.defUsername.",
                "Nome utente Kerberos [{0}]: "},
        {"Kerberos.password.for.username.",
                "Password Kerberos per {0}: "},

        /***    EVERYTHING BELOW IS DEPRECATED  ***/

        // com.sun.security.auth.PolicyFile
        {".error.parsing.", ": errore durante l'analisi "},
        {"COLON", ": "},
        {".error.adding.Permission.", ": errore durante l'aggiunta dell'autorizzazione "},
        {"SPACE", " "},
        {".error.adding.Entry.", ": errore durante l'aggiunta della voce "},
        {"LPARAM", "("},
        {"RPARAM", ")"},
        {"attempt.to.add.a.Permission.to.a.readonly.PermissionCollection",
            "tentativo di aggiungere un'autorizzazione a una PermissionCollection di sola lettura"},

        // com.sun.security.auth.PolicyParser
        {"expected.keystore.type", "tipo keystore previsto"},
        {"can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name",
                "impossibile specificare un principal con una classe carattere jolly senza un nome carattere jolly"},
        {"expected.codeBase.or.SignedBy", "previsto codeBase o SignedBy"},
        {"only.Principal.based.grant.entries.permitted",
                "sono consentiti solo valori garantiti basati sul principal"},
        {"expected.permission.entry", "prevista voce di autorizzazione"},
        {"number.", "numero "},
        {"expected.expect.read.end.of.file.",
                "previsto {0}, letto end of file"},
        {"expected.read.end.of.file", "previsto ';', letto end of file"},
        {"line.", "riga "},
        {".expected.", ": previsto '"},
        {".found.", "', trovato '"},
        {"QUOTE", "'"},

        // SolarisPrincipals
        {"SolarisNumericGroupPrincipal.Primary.Group.",
                "SolarisNumericGroupPrincipal [gruppo primario]: "},
        {"SolarisNumericGroupPrincipal.Supplementary.Group.",
                "SolarisNumericGroupPrincipal [gruppo supplementare]: "},
        {"SolarisNumericUserPrincipal.",
                "SolarisNumericUserPrincipal: "},
        {"SolarisPrincipal.", "SolarisPrincipal: "},
        // provided.null.name is the NullPointerException message when a
        // developer incorrectly passes a null name to the constructor of
        // subclasses of java.security.Principal
        {"provided.null.name", "il nome fornito \u00E8 nullo"}

    };

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     *
     * <p>
     *
     * @return the contents of this <code>ResourceBundle</code>.
     */
    public Object[][] getContents() {
        return contents;
    }
}
