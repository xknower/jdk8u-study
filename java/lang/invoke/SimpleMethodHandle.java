package java.lang.invoke;

import static java.lang.invoke.LambdaForm.BasicType.*;
import static java.lang.invoke.MethodHandleStatics.*;

/**
 * A method handle whose behavior is determined only by its LambdaForm.
 * @author jrose
 */
final class SimpleMethodHandle extends BoundMethodHandle {
    private SimpleMethodHandle(MethodType type, LambdaForm form) {
        super(type, form);
    }

    /*non-public*/ static BoundMethodHandle make(MethodType type, LambdaForm form) {
        return new SimpleMethodHandle(type, form);
    }

    /*non-public*/ static final SpeciesData SPECIES_DATA = SpeciesData.EMPTY;

    /*non-public*/ public SpeciesData speciesData() {
            return SPECIES_DATA;
    }

    @Override
    /*non-public*/ BoundMethodHandle copyWith(MethodType mt, LambdaForm lf) {
        return make(mt, lf);
    }

    @Override
    String internalProperties() {
        return "\n& Class="+getClass().getSimpleName();
    }

    @Override
    /*non-public*/ public int fieldCount() {
        return 0;
    }

    @Override
    /*non-public*/ final BoundMethodHandle copyWithExtendL(MethodType mt, LambdaForm lf, Object narg) {
        return BoundMethodHandle.bindSingle(mt, lf, narg); // Use known fast path.
    }
    @Override
    /*non-public*/ final BoundMethodHandle copyWithExtendI(MethodType mt, LambdaForm lf, int narg) {
        try {
            return (BoundMethodHandle) SPECIES_DATA.extendWith(I_TYPE).constructor().invokeBasic(mt, lf, narg);
        } catch (Throwable ex) {
            throw uncaughtException(ex);
        }
    }
    @Override
    /*non-public*/ final BoundMethodHandle copyWithExtendJ(MethodType mt, LambdaForm lf, long narg) {
        try {
            return (BoundMethodHandle) SPECIES_DATA.extendWith(J_TYPE).constructor().invokeBasic(mt, lf, narg);
        } catch (Throwable ex) {
            throw uncaughtException(ex);
        }
    }
    @Override
    /*non-public*/ final BoundMethodHandle copyWithExtendF(MethodType mt, LambdaForm lf, float narg) {
        try {
            return (BoundMethodHandle) SPECIES_DATA.extendWith(F_TYPE).constructor().invokeBasic(mt, lf, narg);
        } catch (Throwable ex) {
            throw uncaughtException(ex);
        }
    }
    @Override
    /*non-public*/ final BoundMethodHandle copyWithExtendD(MethodType mt, LambdaForm lf, double narg) {
        try {
            return (BoundMethodHandle) SPECIES_DATA.extendWith(D_TYPE).constructor().invokeBasic(mt, lf, narg);
        } catch (Throwable ex) {
            throw uncaughtException(ex);
        }
    }
}
