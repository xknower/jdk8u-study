package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * A {@link ClassVisitor} that merges clinit methods into a single one.
 *
 * @author Eric Bruneton
 */
public class StaticInitMerger extends ClassVisitor {

    private String name;

    private MethodVisitor clinit;

    private final String prefix;

    private int counter;

    public StaticInitMerger(final String prefix, final ClassVisitor cv) {
        this(Opcodes.ASM5, prefix, cv);
    }

    protected StaticInitMerger(final int api, final String prefix,
            final ClassVisitor cv) {
        super(api, cv);
        this.prefix = prefix;
    }

    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName,
            final String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        this.name = name;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name,
            final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv;
        if ("<clinit>".equals(name)) {
            int a = Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC;
            String n = prefix + counter++;
            mv = cv.visitMethod(a, n, desc, signature, exceptions);

            if (clinit == null) {
                clinit = cv.visitMethod(a, name, desc, null, null);
            }
            clinit.visitMethodInsn(Opcodes.INVOKESTATIC, this.name, n, desc,
                    false);
        } else {
            mv = cv.visitMethod(access, name, desc, signature, exceptions);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (clinit != null) {
            clinit.visitInsn(Opcodes.RETURN);
            clinit.visitMaxs(0, 0);
        }
        cv.visitEnd();
    }
}