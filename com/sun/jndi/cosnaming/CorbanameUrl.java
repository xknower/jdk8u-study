package com.sun.jndi.cosnaming;

import javax.naming.Name;
import javax.naming.NamingException;

import java.net.MalformedURLException;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * Extract components of a "corbaname" URL.
 *
 * The format of an corbaname URL is defined in INS 99-12-03 as follows.
 *<p>
 * corbaname url = "corbaname:" <corbaloc_obj> ["#" <string_name>]
 * corbaloc_obj  = <obj_addr_list> ["/" <key_string>]
 * obj_addr_list = as defined in a corbaloc URL
 * key_string    = as defined in a corbaloc URL
 * string_name   = stringified COS name | empty_string
 *<p>
 * Characters in <string_name> are escaped as follows.
 * US-ASCII alphanumeric characters are not escaped. Any characters outside
 * of this range are escaped except for the following:
 *        ; / : ? @ & = + $ , - _ . ! ~ * ; ( )
 * Escaped characters is escaped by using a % followed by its 2 hexadecimal
 * numbers representing the octet.
 *<p>
 * The corbaname URL is parsed into two parts: a corbaloc URL and a COS name.
 * The corbaloc URL is constructed by concatenation "corbaloc:" with
 * <corbaloc_obj>.
 * The COS name is <string_name> with the escaped characters resolved.
 *<p>
 * A corbaname URL is resolved by:
 *<ol>
 *<li>Construct a corbaloc URL by concatenating "corbaloc:" and <corbaloc_obj>.
 *<li>Resolve the corbaloc URL to a NamingContext by using
 *     nctx = ORB.string_to_object(corbalocUrl);
 *<li>Resolve <string_name> in the NamingContext.
 *</ol>
 *
 * @author Rosanna Lee
 */

public final class CorbanameUrl {
    private String stringName;
    private String location;

    /**
     * Returns a possibly empty but non-null string that is the "string_name"
     * portion of the URL.
     */
    public String getStringName() {
        return stringName;
    }

    public Name getCosName() throws NamingException {
        return CNCtx.parser.parse(stringName);
    }

    public String getLocation() {
        return "corbaloc:" + location;
    }

    public CorbanameUrl(String url) throws MalformedURLException {

        if (!url.startsWith("corbaname:")) {
            throw new MalformedURLException("Invalid corbaname URL: " + url);
        }

        int addrStart = 10;  // "corbaname:"

        int addrEnd = url.indexOf('#', addrStart);
        if (addrEnd < 0) {
            addrEnd = url.length();
            stringName = "";
        } else {
            stringName = UrlUtil.decode(url.substring(addrEnd+1));
        }
        location = url.substring(addrStart, addrEnd);

        int keyStart = location.indexOf("/");
        if (keyStart >= 0) {
            // Has key string
            if (keyStart == (location.length() -1)) {
                location += "NameService";
            }
        } else {
            location += "/NameService";
        }
    }
/*
    // for testing only
    public static void main(String[] args) {
        try {
            CorbanameUrl url = new CorbanameUrl(args[0]);

            System.out.println("location: " + url.getLocation());
            System.out.println("string name: " + url.getStringName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
*/
}
