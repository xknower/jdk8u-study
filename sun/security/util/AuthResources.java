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
public class AuthResources extends java.util.ListResourceBundle {

    private static final Object[][] contents = {

        // NT principals
        {"invalid.null.input.value", "invalid null input: {0}"},
        {"NTDomainPrincipal.name", "NTDomainPrincipal: {0}"},
        {"NTNumericCredential.name", "NTNumericCredential: {0}"},
        {"Invalid.NTSid.value", "Invalid NTSid value"},
        {"NTSid.name", "NTSid: {0}"},
        {"NTSidDomainPrincipal.name", "NTSidDomainPrincipal: {0}"},
        {"NTSidGroupPrincipal.name", "NTSidGroupPrincipal: {0}"},
        {"NTSidPrimaryGroupPrincipal.name", "NTSidPrimaryGroupPrincipal: {0}"},
        {"NTSidUserPrincipal.name", "NTSidUserPrincipal: {0}"},
        {"NTUserPrincipal.name", "NTUserPrincipal: {0}"},

        // UnixPrincipals
        {"UnixNumericGroupPrincipal.Primary.Group.name",
                "UnixNumericGroupPrincipal [Primary Group]: {0}"},
        {"UnixNumericGroupPrincipal.Supplementary.Group.name",
                "UnixNumericGroupPrincipal [Supplementary Group]: {0}"},
        {"UnixNumericUserPrincipal.name", "UnixNumericUserPrincipal: {0}"},
        {"UnixPrincipal.name", "UnixPrincipal: {0}"},

        // com.sun.security.auth.login.ConfigFile
        {"Unable.to.properly.expand.config", "Unable to properly expand {0}"},
        {"extra.config.No.such.file.or.directory.",
                "{0} (No such file or directory)"},
        {"Configuration.Error.No.such.file.or.directory",
                "Configuration Error:\n\tNo such file or directory"},
        {"Configuration.Error.Invalid.control.flag.flag",
                "Configuration Error:\n\tInvalid control flag, {0}"},
        {"Configuration.Error.Can.not.specify.multiple.entries.for.appName",
            "Configuration Error:\n\tCan not specify multiple entries for {0}"},
        {"Configuration.Error.expected.expect.read.end.of.file.",
                "Configuration Error:\n\texpected [{0}], read [end of file]"},
        {"Configuration.Error.Line.line.expected.expect.found.value.",
            "Configuration Error:\n\tLine {0}: expected [{1}], found [{2}]"},
        {"Configuration.Error.Line.line.expected.expect.",
            "Configuration Error:\n\tLine {0}: expected [{1}]"},
        {"Configuration.Error.Line.line.system.property.value.expanded.to.empty.value",
            "Configuration Error:\n\tLine {0}: system property [{1}] expanded to empty value"},

        // com.sun.security.auth.module.JndiLoginModule
        {"username.","username: "},
        {"password.","password: "},

        // com.sun.security.auth.module.KeyStoreLoginModule
        {"Please.enter.keystore.information",
                "Please enter keystore information"},
        {"Keystore.alias.","Keystore alias: "},
        {"Keystore.password.","Keystore password: "},
        {"Private.key.password.optional.",
            "Private key password (optional): "},

        // com.sun.security.auth.module.Krb5LoginModule
        {"Kerberos.username.defUsername.",
                "Kerberos username [{0}]: "},
        {"Kerberos.password.for.username.",
                "Kerberos password for {0}: "},

        /***    EVERYTHING BELOW IS DEPRECATED  ***/

        // com.sun.security.auth.PolicyFile
        {".error.parsing.", ": error parsing "},
        {"COLON", ": "},
        {".error.adding.Permission.", ": error adding Permission "},
        {"SPACE", " "},
        {".error.adding.Entry.", ": error adding Entry "},
        {"LPARAM", "("},
        {"RPARAM", ")"},
        {"attempt.to.add.a.Permission.to.a.readonly.PermissionCollection",
            "attempt to add a Permission to a readonly PermissionCollection"},

        // com.sun.security.auth.PolicyParser
        {"expected.keystore.type", "expected keystore type"},
        {"can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name",
                "can not specify Principal with a wildcard class without a wildcard name"},
        {"expected.codeBase.or.SignedBy", "expected codeBase or SignedBy"},
        {"only.Principal.based.grant.entries.permitted",
                "only Principal-based grant entries permitted"},
        {"expected.permission.entry", "expected permission entry"},
        {"number.", "number "},
        {"expected.expect.read.end.of.file.",
                "expected {0}, read end of file"},
        {"expected.read.end.of.file", "expected ';', read end of file"},
        {"line.", "line "},
        {".expected.", ": expected '"},
        {".found.", "', found '"},
        {"QUOTE", "'"},

        // SolarisPrincipals
        {"SolarisNumericGroupPrincipal.Primary.Group.",
                "SolarisNumericGroupPrincipal [Primary Group]: "},
        {"SolarisNumericGroupPrincipal.Supplementary.Group.",
                "SolarisNumericGroupPrincipal [Supplementary Group]: "},
        {"SolarisNumericUserPrincipal.",
                "SolarisNumericUserPrincipal: "},
        {"SolarisPrincipal.", "SolarisPrincipal: "},
        // provided.null.name is the NullPointerException message when a
        // developer incorrectly passes a null name to the constructor of
        // subclasses of java.security.Principal
        {"provided.null.name", "provided null name"}

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
