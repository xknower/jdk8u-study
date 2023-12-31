package sun.nio.cs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

class ISO_8859_1
    extends Charset
    implements HistoricallyNamedCharset
{

    public ISO_8859_1() {
        super("ISO-8859-1", StandardCharsets.aliases_ISO_8859_1);
    }

    public String historicalName() {
        return "ISO8859_1";
    }

    public boolean contains(Charset cs) {
        return ((cs instanceof US_ASCII)
                || (cs instanceof ISO_8859_1));
    }

    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    private static class Decoder extends CharsetDecoder
                                 implements ArrayDecoder {
        private Decoder(Charset cs) {
            super(cs, 1.0f, 1.0f);
        }

        private CoderResult decodeArrayLoop(ByteBuffer src,
                                            CharBuffer dst)
        {
            byte[] sa = src.array();
            int sp = src.arrayOffset() + src.position();
            int sl = src.arrayOffset() + src.limit();
            assert (sp <= sl);
            sp = (sp <= sl ? sp : sl);
            char[] da = dst.array();
            int dp = dst.arrayOffset() + dst.position();
            int dl = dst.arrayOffset() + dst.limit();
            assert (dp <= dl);
            dp = (dp <= dl ? dp : dl);

            try {
                while (sp < sl) {
                    byte b = sa[sp];
                    if (dp >= dl)
                        return CoderResult.OVERFLOW;
                    da[dp++] = (char)(b & 0xff);
                    sp++;
                }
                return CoderResult.UNDERFLOW;
            } finally {
                src.position(sp - src.arrayOffset());
                dst.position(dp - dst.arrayOffset());
            }
        }

        private CoderResult decodeBufferLoop(ByteBuffer src,
                                             CharBuffer dst)
        {
            int mark = src.position();
            try {
                while (src.hasRemaining()) {
                    byte b = src.get();
                    if (!dst.hasRemaining())
                        return CoderResult.OVERFLOW;
                    dst.put((char)(b & 0xff));
                    mark++;
                }
                return CoderResult.UNDERFLOW;
            } finally {
                src.position(mark);
            }
        }

        protected CoderResult decodeLoop(ByteBuffer src,
                                         CharBuffer dst)
        {
            if (src.hasArray() && dst.hasArray())
                return decodeArrayLoop(src, dst);
            else
                return decodeBufferLoop(src, dst);
        }

        public int decode(byte[] src, int sp, int len, char[] dst) {
            if (len > dst.length)
                len = dst.length;
            int dp = 0;
            while (dp < len)
                dst[dp++] = (char)(src[sp++] & 0xff);
            return dp;
        }
    }

    private static class Encoder extends CharsetEncoder
                                 implements ArrayEncoder {
        private Encoder(Charset cs) {
            super(cs, 1.0f, 1.0f);
        }

        public boolean canEncode(char c) {
            return c <= '\u00FF';
        }

        public boolean isLegalReplacement(byte[] repl) {
            return true;  // we accept any byte value
        }

        private final Surrogate.Parser sgp = new Surrogate.Parser();

        // JVM may replace this method with intrinsic code.
        // don't pass len value <= 0
        private static int encodeISOArray(char[] sa, int sp,
                                          byte[] da, int dp, int len)
        {
            int i = 0;
            for (; i < len; i++) {
                char c = sa[sp++];
                if (c > '\u00FF')
                    break;
                da[dp++] = (byte)c;
            }
            return i;
        }

        private CoderResult encodeArrayLoop(CharBuffer src,
                                            ByteBuffer dst)
        {
            char[] sa = src.array();
            int soff = src.arrayOffset();
            int sp = soff + src.position();
            int sl = soff + src.limit();
            assert (sp <= sl);
            sp = (sp <= sl ? sp : sl);
            byte[] da = dst.array();
            int doff = dst.arrayOffset();
            int dp = doff + dst.position();
            int dl = doff + dst.limit();
            assert (dp <= dl);
            dp = (dp <= dl ? dp : dl);
            int dlen = dl - dp;
            int slen = sl - sp;
            int len  = (dlen < slen) ? dlen : slen;
            try {
                int ret = (len <= 0) ? 0 : encodeISOArray(sa, sp, da, dp, len);
                sp = sp + ret;
                dp = dp + ret;
                if (ret != len) {
                    if (sgp.parse(sa[sp], sa, sp, sl) < 0)
                        return sgp.error();
                    return sgp.unmappableResult();
                }
                if (len < slen)
                    return CoderResult.OVERFLOW;
                return CoderResult.UNDERFLOW;
            } finally {
                src.position(sp - soff);
                dst.position(dp - doff);
            }
        }

        private CoderResult encodeBufferLoop(CharBuffer src,
                                             ByteBuffer dst)
        {
            int mark = src.position();
            try {
                while (src.hasRemaining()) {
                    char c = src.get();
                    if (c <= '\u00FF') {
                        if (!dst.hasRemaining())
                            return CoderResult.OVERFLOW;
                        dst.put((byte)c);
                        mark++;
                        continue;
                    }
                    if (sgp.parse(c, src) < 0)
                        return sgp.error();
                    return sgp.unmappableResult();
                }
                return CoderResult.UNDERFLOW;
            } finally {
                src.position(mark);
            }
        }

        protected CoderResult encodeLoop(CharBuffer src,
                                         ByteBuffer dst)
        {
            if (src.hasArray() && dst.hasArray())
                return encodeArrayLoop(src, dst);
            else
                return encodeBufferLoop(src, dst);
        }

        private byte repl = (byte)'?';
        protected void implReplaceWith(byte[] newReplacement) {
            repl = newReplacement[0];
        }

        public int encode(char[] src, int sp, int len, byte[] dst) {
            int dp = 0;
            int slen = Math.min(len, dst.length);
            int sl = sp + slen;
            while (sp < sl) {
                int ret =
                    (slen <= 0) ? 0 : encodeISOArray(src, sp, dst, dp, slen);
                sp = sp + ret;
                dp = dp + ret;
                if (ret != slen) {
                    char c = src[sp++];
                    if (Character.isHighSurrogate(c) && sp < sl &&
                        Character.isLowSurrogate(src[sp])) {
                        if (len > dst.length) {
                            sl++;
                            len--;
                        }
                        sp++;
                    }
                    dst[dp++] = repl;
                    slen = Math.min((sl - sp), (dst.length - dp));
                }
            }
            return dp;
        }
    }
}
