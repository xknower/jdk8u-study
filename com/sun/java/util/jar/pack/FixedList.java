package com.sun.java.util.jar.pack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
 * @author ksrini
 */

/*
 * This class provides an ArrayList implementation which has a fixed size,
 * thus all the operations which modifies the size have been rendered
 * inoperative. This essentially allows us to use generified array
 * lists in lieu of arrays.
 */
final class FixedList<E> implements List<E> {

    private final ArrayList<E> flist;

    protected FixedList(int capacity) {
        flist = new ArrayList<>(capacity);
        // initialize the list to null
        for (int i = 0 ; i < capacity ; i++) {
            flist.add(null);
        }
    }
    @Override
    public int size() {
        return flist.size();
    }

    @Override
    public boolean isEmpty() {
        return flist.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return flist.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return flist.iterator();
    }

    @Override
    public Object[] toArray() {
        return flist.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return flist.toArray(a);
    }

    @Override
    public boolean add(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public boolean remove(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return flist.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) throws UnsupportedOperationException {
         throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public boolean removeAll(Collection<?> c)  throws UnsupportedOperationException  {
         throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public boolean retainAll(Collection<?> c)  throws UnsupportedOperationException  {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public void clear()  throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public E get(int index) {
        return flist.get(index);
    }

    @Override
    public E set(int index, E element) {
        return flist.set(index, element);
    }

    @Override
    public void add(int index, E element)  throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public E remove(int index)   throws UnsupportedOperationException {
        throw new UnsupportedOperationException("operation not permitted");
    }

    @Override
    public int indexOf(Object o) {
        return flist.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return flist.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return flist.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return flist.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return flist.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return "FixedList{" + "plist=" + flist + '}';
    }
}

