package sun.tools.jstat;

import java.io.StreamTokenizer;

/**
 * A class for encapsulating tokens returned from StreamTokenizer primarily
 * for output formatting purposes.
 *
 * @author Brian Doherty
 * @since 1.5
 */
public class Token {
    public String sval;
    public double nval;
    public int ttype;

    public Token(int ttype, String sval, double nval) {
        this.ttype = ttype;
        this.sval = sval;
        this.nval = nval;
    }

    public Token(int ttype, String sval) {
        this(ttype, sval, 0);
    }

    public Token(int ttype) {
        this(ttype, null, 0);
    }

    public String toMessage() {
        switch(ttype) {
        case StreamTokenizer.TT_EOL:
            return "\"EOL\"";
        case StreamTokenizer.TT_EOF:
            return "\"EOF\"";
        case StreamTokenizer.TT_NUMBER:
            return "NUMBER";
        case StreamTokenizer.TT_WORD:
            if (sval == null) {
                return "IDENTIFIER";
            } else {
                return "IDENTIFIER " + sval;
            }
        default:
            if (ttype == (int)'"') {
                String msg = "QUOTED STRING";
                if (sval != null)
                    msg = msg + " \"" + sval + "\"";
                return msg;
            } else {
                return "CHARACTER \'" + (char)ttype + "\'";
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(ttype) {
        case StreamTokenizer.TT_EOL:
            sb.append("ttype=TT_EOL");
            break;
        case StreamTokenizer.TT_EOF:
            sb.append("ttype=TT_EOF");
            break;
        case StreamTokenizer.TT_NUMBER:
            sb.append("ttype=TT_NUM,").append("nval="+nval);
            break;
        case StreamTokenizer.TT_WORD:
            if (sval == null) {
                sb.append("ttype=TT_WORD:IDENTIFIER");
            } else {
                sb.append("ttype=TT_WORD:").append("sval="+sval);
            }
            break;
        default:
            if (ttype == (int)'"') {
                sb.append("ttype=TT_STRING:").append("sval="+sval);
            } else {
                sb.append("ttype=TT_CHAR:").append((char)ttype);
            }
            break;
        }
        return sb.toString();
    }
}
