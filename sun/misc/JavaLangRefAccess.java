package sun.misc;

public interface JavaLangRefAccess {

    /**
     * Help ReferenceHandler thread process next pending
     * {@link java.lang.ref.Reference}
     *
     * @return {@code true} if there was a pending reference and it
     *         was enqueue-ed or {@code false} if there was no
     *         pending reference
     */
    boolean tryHandlePendingReference();
}
