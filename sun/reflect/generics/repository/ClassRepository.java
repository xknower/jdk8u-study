package sun.reflect.generics.repository;

import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.ClassSignature;
import sun.reflect.generics.tree.TypeTree;
import sun.reflect.generics.visitor.Reifier;
import sun.reflect.generics.parser.SignatureParser;
import java.lang.reflect.Type;


/**
 * This class represents the generic type information for a class.
 * The code is not dependent on a particular reflective implementation.
 * It is designed to be used unchanged by at least core reflection and JDI.
 */
public class ClassRepository extends GenericDeclRepository<ClassSignature> {

    public static final ClassRepository NONE = ClassRepository.make("Ljava/lang/Object;", null);

    /** The generic superclass info.  Lazily initialized. */
    private volatile Type superclass;

    /** The generic superinterface info.  Lazily initialized. */
    private volatile Type[] superInterfaces;

 // private, to enforce use of static factory
    private ClassRepository(String rawSig, GenericsFactory f) {
        super(rawSig, f);
    }

    protected ClassSignature parse(String s) {
        return SignatureParser.make().parseClassSig(s);
    }

    /**
     * Static factory method.
     * @param rawSig - the generic signature of the reflective object
     * that this repository is servicing
     * @param f - a factory that will provide instances of reflective
     * objects when this repository converts its AST
     * @return a <tt>ClassRepository</tt> that manages the generic type
     * information represented in the signature <tt>rawSig</tt>
     */
    public static ClassRepository make(String rawSig, GenericsFactory f) {
        return new ClassRepository(rawSig, f);
    }

    // public API
 /*
 * When queried for a particular piece of type information, the
 * general pattern is to consult the corresponding cached value.
 * If the corresponding field is non-null, it is returned.
 * If not, it is created lazily. This is done by selecting the appropriate
 * part of the tree and transforming it into a reflective object
 * using a visitor.
 * a visitor, which is created by feeding it the factory
 * with which the repository was created.
 */

    public Type getSuperclass() {
        Type superclass = this.superclass;
        if (superclass == null) { // lazily initialize superclass
            Reifier r = getReifier(); // obtain visitor
            // Extract superclass subtree from AST and reify
            getTree().getSuperclass().accept(r);
            // extract result from visitor and cache it
            superclass = r.getResult();
            this.superclass = superclass;
        }
        return superclass; // return cached result
    }

    public Type[] getSuperInterfaces() {
        Type[] superInterfaces = this.superInterfaces;
        if (superInterfaces == null) { // lazily initialize super interfaces
            // first, extract super interface subtree(s) from AST
            TypeTree[] ts  = getTree().getSuperInterfaces();
            // create array to store reified subtree(s)
            superInterfaces = new Type[ts.length];
            // reify all subtrees
            for (int i = 0; i < ts.length; i++) {
                Reifier r = getReifier(); // obtain visitor
                ts[i].accept(r);// reify subtree
                // extract result from visitor and store it
                superInterfaces[i] = r.getResult();
            }
            this.superInterfaces = superInterfaces;
        }
        return superInterfaces.clone(); // return cached result
    }
}
