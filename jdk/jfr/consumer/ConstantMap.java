package jdk.jfr.consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds mapping between a set of keys and their corresponding object.
 *
 * If the type is a known type, i.e. {@link RecordedThread}, an
 * {@link ObjectFactory} can be supplied which will instantiate a typed object.
 */
final class ConstantMap {
    private final static class Reference {
        private final long key;
        private final ConstantMap pool;

        Reference(ConstantMap pool, long key) {
            this.pool = pool;
            this.key = key;
        }

        Object resolve() {
            return pool.get(key);
        }
    }

    private final ObjectFactory<?> factory;
    private final LongMap<Object> objects;

    private LongMap<Boolean> isResolving;
    private boolean allResolved;
    private String name;

    ConstantMap(ObjectFactory<?> factory, String name) {
        this.name = name;
        this.objects = new LongMap<>();
        this.factory = factory;
    }

    Object get(long id) {
        // fast path, all objects in pool resolved
        if (allResolved) {
            return objects.get(id);
        }
        // referenced from a pool, deal with this later
        if (isResolving == null) {
            return new Reference(this, id);
        }

        Boolean beingResolved = isResolving.get(id);

        // we are resolved (but not the whole pool)
        if (Boolean.FALSE.equals(beingResolved)) {
            return objects.get(id);
        }

        // resolving ourself, abort to avoid infinite recursion
        if (Boolean.TRUE.equals(beingResolved)) {
            return null;
        }

        // resolve me!
        isResolving.put(id, Boolean.TRUE);
        Object resolved = resolve(objects.get(id));
        isResolving.put(id, Boolean.FALSE);
        if (factory != null) {
            Object factorized = factory.createObject(id, resolved);
            objects.put(id, factorized);
            return factorized;
        } else {
            objects.put(id, resolved);
            return resolved;
        }
    }

    private static Object resolve(Object o) {
        if (o instanceof Reference) {
            return resolve(((Reference) o).resolve());
        }
        if (o != null && o.getClass().isArray()) {
            final Object[] array = (Object[]) o;
            for (int i = 0; i < array.length; i++) {
                array[i] = resolve(array[i]);
            }
            return array;
        }
        return o;
    }

    public void resolve() {
        List<Long> keyList = new ArrayList<>();
        objects.keys().forEachRemaining(keyList::add);
        for (Long l : keyList) {
            get(l);
        }
    }

    public void put(long key, Object value) {
        objects.put(key, value);
    }

    public void setIsResolving() {
        isResolving = new LongMap<>();
    }

    public void setResolved() {
        allResolved = true;
        isResolving = null; // pool finished, release memory
    }

    public String getName() {
        return name;
    }
}
