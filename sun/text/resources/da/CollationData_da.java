package sun.text.resources.da;

import java.util.ListResourceBundle;

public class CollationData_da extends ListResourceBundle {

        protected final Object[][] getContents() {
                return new Object[][] {
                        { "Rule",
                                "& A;\u00C1;\u00C0;\u00C2,a;\u00E1;\u00E0;\u00E2" // A; A-acute; A-grave; A-circ, a; a-acute, a-grave, a-circ
                                        + "<B,b"
                                        + "<C;\u00c7,c;\u00e7" // c-cedill
                                        + "<D;\u00D0;\u0110,d;\u00F0;\u0111" // eth(icelandic), d-stroke (sami); they looks identically
                                        + "<E;\u00C9;\u00C8;\u00CA;\u00CB,e;\u00E9;\u00E8;\u00EA;\u00EB" // E; E-acute; E-grave; E-circ; E-diaeresis, e-acute, e-grave; e-circ; e-diaeresis
                                        + "<F,f <G,g <H,h"
                                        + "<I;\u00CD,i;\u00ED" // i-acute
                                        + "<J,j <K,k <L,l <M,m <N,n"
                                        + "<O;\u00D3;\u00d4,o;\u00F3;\u00f4" // o-acute, o-circ
                                        + "<P,p <Q,q <R,r <S,s <T,t"
                                        + "<U,u <V,v <W,w <X,x"
                                        + "<Y;\u00DD;U\u0308,y;\u00FD;u\u0308" // y-acute, u-diaeresis
                                        + "<Z,z"
                                        // ae-ligature and variants
                                        + "<\u00c6,\u00e6" // ae-ligature
                                        + ";\u00c6\u0301,\u00e6\u0301" // ae-acute
                                        + ";A\u0308,a\u0308 "       // a-diaeresis
                                        // o-stroke and variant
                                        + "<\u00d8,\u00f8 " // o-slash
                                        + ";\u00d8\u0301,\u00f8\u0301" // o-slash-acute
                                        + ";O\u0308,o\u0308 "  // ; o-diaeresis
                                        + ";O\u030b,o\u030b"        // nt :  o-double-acute
                                        // a-ring and variants
                                        + "< \u00c5 , \u00e5"       // a-ring
                                        + ";\u00c5\u0301,\u00e5\u0301" // a-ring-acute
                                        + ", AA , Aa , aA , aa "      // after a-ring
                                        + "& ss;\u00DF"             // s-zet
                                        + "& th, \u00FE & th, \u00DE "     // thorn
                                        + "& oe, \u0153 & oe, \u0152 " // oe-ligature
                        }
                };
        }
}