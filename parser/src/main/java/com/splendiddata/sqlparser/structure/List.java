/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

import java.util.Arrays;

/**
 * List as defined in /postgresql-9.3.4/src/include/nodes/pg_list.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class List<T> extends Node implements java.util.List<T>, Serializable {
    private static final long serialVersionUID = 400L;

    protected int length;
    ListCell<T> head;
    ListCell<T> tail;

    protected int updateCount;

    /**
     * Constructor
     */
    public List() {
        super(NodeTag.T_List);
    }

    /**
     * Copy constructor
     *
     * @param other
     *            The list to copy
     */
    public List(Collection<T> other) {
        super(NodeTag.T_List);
        if (other instanceof List) {
            super.location = ((List<T>) other).location;
        }
        for (T t : other) {
            add(t);
        }
    }

    /**
     * Creates a copy of this List.
     * <p>
     * For each element of the list, an attempt will be done to invoke Ts clone method. If that succeeds, the cloned
     * object will be added to the cloned list. Otherwise the original object will be added to the cloned list.
     * </p>
     * 
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     *
     * @return List&lt;T&gt; A copied list.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> clone() {
        List<T> clone = (List<T>) super.clone();
        clone.clear();
        for (T element : this) {
            if (element == null) {
                clone.add(null);
                continue;
            }
            try {
                Method cloneMethod = element.getClass().getMethod("clone");
                clone.add((T) cloneMethod.invoke(element));
            } catch (Exception e) {
                clone.add(element);
            }

        }
        return clone;
    }

    /**
     * Returns the ListCell from the head of the list
     *
     * @return ListCell&lt;T&gt; or null if the list is empty
     */
    public ListCell<T> getHead() {
        return head;
    }

    /**
     * Returns the ListCell from the tail of the list
     *
     * @return ListCell&lt;T&gt; or null if the list is empty
     */
    public ListCell<T> getTail() {
        return tail;
    }

    /**
     * @see java.util.List#size()
     *
     * @return int the number of entries contained in this list
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * @see java.util.List#isEmpty()
     *
     * @return boolean true if the list does not contain any entries
     */
    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * @see java.util.List#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (o == null ? cell.data == null : o.equals(cell.data)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see java.util.List#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new InternalListIterator(0);
    }

    /**
     * @see java.util.List#toArray()
     */
    @Override
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[length];
        ListCell<T> cell = head;
        for (int i = 0; i < length; i++) {
            arr[i] = cell.data;
            cell = cell.next;
        }
        return arr;
    }

    /**
     * just throws UnsupportedOperationException.
     */
    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray(Object[] a) {
        T[] result = (T[]) a;
        if (result.length < length) {
            result = (T[]) Array.newInstance(a.getClass().getComponentType(), length);
        }
        int i = 0;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            result[i++] = cell.data;
        }
        while (i < a.length) {
            result[i++] = null;
        }
        return result;
    }

    /**
     * Adds he new element to the tail of the list
     * 
     * @see java.util.List#add(java.lang.Object)
     * 
     * @param e
     *            the new element
     * @return boolean true
     */
    @Override
    public boolean add(T e) {
        updateCount++;
        ListCell<T> newCell = new ListCell<>();
        newCell.data = e;
        if (head == null) {
            head = newCell;
            tail = newCell;
        } else {
            tail.next = newCell;
            tail = newCell;
        }
        length++;
        return true;
    }

    /**
     * @see java.util.List#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        updateCount++;
        ListCell<T> previous = null;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (o == null ? cell.data == null : o.equals(cell.data)) {
                length--;
                if (previous == null) {
                    head = cell.next;
                    if (head == null) {
                        tail = null;
                    }
                } else {
                    previous.next = cell.next;
                    if (previous.next == null) {
                        tail = previous;
                    }
                }
                return true;
            }
            previous = cell;
        }
        return false;
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            return true;
        }
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        updateCount++;
        if (c.isEmpty()) {
            return false;
        }

        Iterator<? extends T> T = c.iterator();
        if (head == null) {
            head = new ListCell<>();
            head.data = T.next();
            tail = head;
        }
        while (T.hasNext()) {
            tail.next = new ListCell<>();
            tail = tail.next;
            tail.data = T.next();
        }
        length += c.size();
        return true;
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        updateCount++;
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " should have been between 0 and size() (=" + length + ')');
        }
        if (c.isEmpty()) {
            return false;
        }

        ListCell<T> cell;
        ListCell<T> newCell;
        Iterator<? extends T> T = c.iterator();
        if (index == 0) {
            cell = new ListCell<>();
            cell.data = T.next();
            cell.next = head;
            head = cell;
        } else {
            cell = head;
            for (int i = 1; i < index; i++) {
                cell = cell.next;
            }
        }
        while (T.hasNext()) {
            newCell = new ListCell<>();
            newCell.data = T.next();
            newCell.next = cell.next;
            cell.next = newCell;
            cell = newCell;
        }
        if (cell.next == null) {
            tail = cell;
        }
        length += c.size();
        return true;
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        updateCount++;
        boolean result = false;
        for (Object element : c) {
            tail = null;
            for (ListCell<T> cell = head; cell != null; cell = cell.next) {
                if (Objects.deepEquals(cell.data, element)) {
                    result = true;
                    length--;
                    if (tail == null) {
                        head = cell.next;
                    } else {
                        tail.next = cell.next;
                    }
                } else {
                    tail = cell;
                }
            }
        }
        return result;
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        updateCount++;
        boolean result = false;
        ListCell<T> previous = null;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (c.contains(cell.data)) {
                previous = cell;
            } else {
                result = true;
                length--;
                if (previous == null) {
                    head = cell.next;
                    if (head == null) {
                        tail = null;
                    }
                } else {
                    previous.next = cell.next;
                    if (previous.next == null) {
                        tail = previous;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @see java.util.List#clear()
     */
    @Override
    public void clear() {
        updateCount++;
        length = 0;
        head = null;
        tail = null;
    }

    /**
     * @see java.util.List#get(int)
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index " + index + " should have been >= 0 and < " + length);
        }
        if (index == 0) {
            return head.data;
        }
        ListCell<T> cell = head;
        for (int i = 0; i < index; i++) {
            cell = cell.next;
        }
        return cell.data;
    }

    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index " + index + " should have been >= 0 and < " + length);
        }
        T result;
        if (index == 0) {
            result = head.data;
            head.data = element;
        } else {
            ListCell<T> cell = head;
            for (int i = 0; i < index; i++) {
                cell = cell.next;
            }
            result = cell.data;
            cell.data = element;
        }
        return result;
    }

    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    @Override
    public void add(int index, T element) {
        updateCount++;
        if (index < 0 || index > length) {
            throw new IndexOutOfBoundsException();
        }
        ListCell<T> newCell = new ListCell<>();
        newCell.data = element;
        if (index == 0) {
            newCell.next = head;
            head = newCell;
            if (tail == null) {
                tail = newCell;
            }
        } else {
            ListCell<T> prior = head;
            for (int i = 1; i < index; i++) {
                prior = prior.next;
            }
            newCell.next = prior.next;
            prior.next = newCell;
            if (newCell.next == prior) {
                tail = newCell;
            }
        }
        length++;
    }

    /**
     * @see java.util.List#remove(int)
     */
    @Override
    public T remove(int index) {
        updateCount++;
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }
        ListCell<T> prior = null;
        ListCell<T> cell = head;
        for (int i = 0; i < index; i++) {
            prior = cell;
            cell = cell.next;
        }
        if (prior == null) {
            head = cell.next;
        } else {
            prior.next = cell.next;
        }
        if (tail == cell) {
            tail = prior;
        }
        length--;
        return cell.data;
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    @Override
    public int indexOf(Object o) {
        int result = 0;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (o == null ? cell.data == null : o.equals(cell.data)) {
                return result;
            }
            ++result;
        }
        return -1;
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    @Override
    public int lastIndexOf(Object o) {
        int result = -1;
        int i = 0;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (o == null ? cell.data == null : o.equals(cell.data)) {
                result = i;
            }
            ++i;
        }
        return result;
    }

    /**
     * @see java.util.List#listIterator()
     */
    @Override
    public ListIterator<T> listIterator() {
        return new InternalListIterator(0);
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        return new InternalListIterator(index);
    }

    /**
     * @see java.util.List#subList(int, int)
     */
    @Override
    public java.util.List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > length || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        return new Sublist(fromIndex, toIndex);
    }

    /**
     * Delete 'cell' from 'list'; 'prev' is the previous element to 'cell' in 'list', if any (i.e. prev == NULL iff
     * list-&gt;head == cell)
     * <p>
     * The cell is pfree'd, as is the List header if this was the last member.
     * </p>
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * </p>
     * 
     * @param cell
     *            The ListCell to delete
     * @param prev
     *            The ListCell in front of the cell to delete. this holds a pointer to the cell to delete that is to be
     *            overwrTten by the next pointer from the cell to be deleted.
     * @return List&lt;T&gt; this or null if the list is empty
     */
    public List<T> list_delete_cell(ListCell<T> cell, ListCell<T> prev) {
        updateCount++;
        if (length == 1) {
            length = 0;
            head = tail = null;
            return null;
        }

        length--;
        if (prev == null) {
            head = cell;
        } else {
            prev.next = cell.next;
        }
        if (tail == cell) {
            tail = prev;
        }
        return this;
    }

    /**
     * Truncate 'list' to contain no more than 'new_size' elements. This modifies the list in-place! DespTe this,
     * callers should use the pointer returned by this function to refer to the newly truncated list -- T may or may not
     * be the same as the pointer that was passed.
     * <p>
     * Copied from /postgresql-9.3.4/src/backend/nodes/list.c
     * </p>
     *
     * @param new_size
     *            The number of entries to keep
     * @return List&lt;T&gt; this
     */
    public List<T> list_truncate(int new_size) {
        updateCount++;
        if (new_size <= 0) {
            head = tail = null;
            length = 0;
            return null; /* truncate to zero length */
        }

        /* If asked to effectively extend the list, do nothing */
        if (new_size >= length) {
            return this;
        }

        int n = 1;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (n == new_size) {
                cell.next = null;
                tail = cell;
                length = new_size;
                return this;
            }
            n++;
        }

        /* keep the compiler quiet; never reached */
        return this;
    }

    /**
     * @see java.util.Collection#stream()
     *
     * @return Stream&lt;T&gt; A Stream of data entries
     */
    public Stream<T> stream() {
        return StreamSupport.stream(new Spliterator<T>() {
            private ListCell<T> nextCell = head;

            /**
             * @see java.util.Spliterator#tryAdvance(java.util.function.Consumer)
             *
             * @param action
             *            The Consumer&lt;? super T&gt; that does what it needs to do on the next list cell content
             * @return true if a list cell content is provided to the action (may be null) or false at the end of the
             *         stream
             */
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (nextCell == null) {
                    return false;
                }
                T cellContent = nextCell.data;
                nextCell = nextCell.next;
                action.accept(cellContent);
                return true;
            }

            /**
             * @see java.util.Spliterator#trySplit()
             *
             * @return null as splitting a node list (sql statement) on an arbitrary place makes no sense
             */
            @Override
            public Spliterator<T> trySplit() {
                return null;
            }

            /**
             * @see java.util.Spliterator#estimateSize()
             *
             * @return long the size of the list
             */
            @Override
            public long estimateSize() {
                return length;
            }

            /**
             * @see java.util.Spliterator#characteristics()
             *
             * @return IMMUTABLE | NONNULL | ORDERED
             */
            @Override
            public int characteristics() {
                return IMMUTABLE | NONNULL | ORDERED;
            }

            /**
             * @see java.util.Spliterator#getComparator()
             *
             * @return null as comparing Nodes in a list makes no sense
             */
            @Override
            public Comparator<? super T> getComparator() {
                return null;
            }
        }, false);
    }

    @Override
    public String toString() {
        if (head == null) {
            return "()";
        }
        StringBuilder result = new StringBuilder();
        String separator = "(";

        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            result.append(separator).append(cell.data);
            separator = ", ";

        }
        result.append(')');
        return result.toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (ListCell<T> cell = head; cell != null; cell = cell.next) {
            if (cell.data != null) {
                hashCode = 31 * hashCode + (cell.data == null ? 0 : cell.data.hashCode());
            }
        }
        return hashCode;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof java.util.List)) {
            return false;
        }
        if (length != ((java.util.List<?>) obj).size()) {
            return false;
        }
        if (!(obj instanceof List)) {
            Iterator<Object> it = ((java.util.List<Object>) obj).iterator();
            for (ListCell<T> cell = head; cell != null; cell = cell.next) {
                if (!Objects.deepEquals(cell.data, it.next())) {
                    return false;
                }
            }
            return true;
        }
        if (!Objects.deepEquals(location, ((List<Object>) obj).location)) {
            return false;
        }
        ListCell<T> myCell = head;
        for (ListCell<Object> othersCell = ((List<Object>) obj).head; othersCell != null; othersCell = othersCell.next) {
            if (!Objects.deepEquals(myCell.data, othersCell.data)) {
                return false;
            }
            myCell = myCell.next;
        }
        return true;
    }

    /**
     * Returns the this pointer for debugging purposes - to print the list content in an xml format.
     *
     * @return List&lt;T&gt; this
     */
    @XmlAnyElement
    private List<T> getThis() {
        return this;
    }

    private class InternalListIterator implements ListIterator<T> {
        private ListCell<T> cell;
        private int index = -1;
        private int cursor;

        private int atUpdateCount;

        /**
         * Constructor
         */
        public InternalListIterator(int ind) {
            if (ind < 0 || ind > length) {
                throw new IndexOutOfBoundsException();
            }
            atUpdateCount = updateCount;
            while (index < ind - 1) {
                next();
            }
        }

        /**
         * @see java.util.ListTerator#hasNext()
         */
        @Override
        public boolean hasNext() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return cursor < length;
        }

        /**
         * @see java.util.ListTerator#next()
         */
        @Override
        public T next() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor == index) {
                cursor++;
                return cell.data;
            }
            if (cursor < length) {
                index = cursor;
                cell = index == 0 ? (ListCell<T>) head : cell.next;
                cursor++;
                return cell.data;
            }
            throw new NoSuchElementException();
        }

        /**
         * @see java.util.ListTerator#hasPrevious()
         */
        @Override
        public boolean hasPrevious() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return cursor != 0;
        }

        /**
         * @see java.util.ListTerator#previous()
         */
        @Override
        public T previous() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (cursor <= 0) {
                throw new NoSuchElementException();
            }
            index = cursor - 1;
            cell = head;
            for (int i = 0; i < index; i++) {
                cell = cell.next;
            }
            cursor = index;
            return cell.data;
        }

        /**
         * @see java.util.ListTerator#nextIndex()
         */
        @Override
        public int nextIndex() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return cursor;
        }

        /**
         * @see java.util.ListTerator#previousIndex()
         */
        @Override
        public int previousIndex() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return cursor - 1;
        }

        /**
         * @see java.util.ListTerator#remove()
         */
        @Override
        public void remove() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0) {
                throw new IllegalStateException();
            }
            if (index == 0) {
                head = head.next;
                if (head == null) {
                    tail = null;
                }
            } else {
                ListCell<T> toBeRemoved = head;
                for (int i = 0; i < index; i++) {
                    cell = toBeRemoved;
                    toBeRemoved = cell.next;
                }
                cell.next = toBeRemoved.next;
                if (cell.next == null) {
                    tail = cell;
                }
            }
            cursor = index;
            length--;
            atUpdateCount = ++updateCount;
            index = -1;
        }

        /**
         * @see java.util.ListTerator#set(java.lang.Object)
         */
        @Override
        public void set(T e) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0) {
                throw new IllegalStateException();
            }
            if (index == 0) {
                cell = head;
            }
            cell.data = e;
            atUpdateCount = ++updateCount;
        }

        /**
         * @see java.util.ListTerator#add(java.lang.Object)
         */
        @Override
        public void add(T e) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            ListCell<T> newCell = new ListCell<>();
            newCell.data = e;
            if (cursor == 0) {
                newCell.next = head;
                head = newCell;
                if (tail == null) {
                    tail = newCell;
                }
            } else {
                cell = head;
                for (int i = 1; i < cursor; i++) {
                    cell = cell.next;
                }
                newCell.next = cell.next;
                cell.next = newCell;
                if (newCell.next == null) {
                    tail = newCell;
                }
            }
            cell = newCell;
            cursor++;
            length++;
            atUpdateCount = ++updateCount;
            index = -1;
        }
    }

    private class Sublist implements java.util.List<T> {
        private final int fromIndex;
        protected int sublistLength;
        protected ListCell<T> cellBefore;
        private ListCell<T> cellAfter;
        private int atUpdateCount;

        /**
         * Constructor
         *
         * @param fromIndex
         *            Start index of this sublist
         * @param untilIndex
         *            Tail index of this sublist
         */
        Sublist(int fromIndex, int untilIndex) {
            this.fromIndex = fromIndex;
            this.sublistLength = untilIndex - fromIndex;

            if (fromIndex == 0) {
                cellBefore = null;
            } else {
                cellBefore = head;
                for (int i = 1; i < fromIndex; i++) {
                    cellBefore = cellBefore.next;
                }
            }
            if (untilIndex == 0) {
                cellAfter = null;
            } else {
                cellAfter = head;
                for (int i = 0; i < untilIndex; i++) {
                    cellAfter = cellAfter.next;
                }
            }
            atUpdateCount = updateCount;
        }

        /**
         * @see java.util.List#size()
         */
        @Override
        public int size() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return sublistLength;
        }

        /**
         * @see java.util.List#isEmpty()
         */
        @Override
        public boolean isEmpty() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return sublistLength == 0;
        }

        /**
         * @see java.util.List#contains(java.lang.Object)
         */
        @Override
        public boolean contains(Object o) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (sublistLength == 0) {
                return false;
            }
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (o == null ? cell.data == null : o.equals(cell.data)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @see java.util.List#iterator()
         *
         * @return
         */
        @Override
        public Iterator<T> iterator() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return new InternalSublistIterator(0);
        }

        /**
         * @see java.util.List#toArray()
         */
        @Override
        public Object[] toArray() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            Object[] result = new Object[sublistLength];
            if (sublistLength > 0) {
                ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
                for (int i = 0; i < sublistLength; i++) {
                    result[i++] = cell.data;
                    cell = cell.next;
                }
            }
            return result;
        }

        /**
         * @see java.util.List#toArray(java.lang.Object[])
         */
        @SuppressWarnings("unchecked")
        @Override
        public <A> A[] toArray(A[] a) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (sublistLength == 0) {
                Arrays.fill(a, null);
                return a;
            }
            Object[] result = a;
            if (result.length < sublistLength) {
                result = (Object[]) Array.newInstance(a.getClass().getComponentType(), sublistLength);
            }
            int i = 0;
            ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
            for (; i < sublistLength; i++) {
                result[i++] = cell.data;
                cell = cell.next;
            }
            while (i < result.length) {
                result[i++] = null;
            }
            return (A[]) result;
        }

        /**
         * @see java.util.List#add(java.lang.Object)
         */
        @Override
        public boolean add(T e) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }

            ListCell<T> newCell = new ListCell<>();
            newCell.data = e;
            if (sublistLength == 0) {
                if (cellBefore == null) {
                    newCell.next = head;
                    head = newCell;
                } else {
                    newCell.next = cellBefore.next;
                    cellBefore.next = newCell;
                }
            } else {
                ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
                while (cell.next != cellAfter) {
                    cell = cell.next;
                }
                newCell.next = cell.next;
                cell.next = newCell;
            }
            if (newCell.next == null) {
                tail = newCell;
            }
            incrementLength(1);
            return true;
        }

        /**
         * @see java.util.List#remove(java.lang.Object)
         */
        @Override
        public boolean remove(Object o) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            ListCell<T> previous = cellBefore;
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (o == null ? cell.data == null : o.equals(cell.data)) {
                    if (previous == null) {
                        head = cell.next;
                    } else {
                        previous.next = cell;
                    }
                    if (cell.next == null) {
                        tail = previous;
                    }
                    incrementLength(-1);
                    return true;
                }
                previous = cell;
            }
            return false;
        }

        /**
         * @see java.util.List#containsAll(java.util.Collection)
         */
        @Override
        public boolean containsAll(Collection<?> c) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            for (Object obj : c) {
                if (!contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @see java.util.List#addAll(java.util.Collection)
         */
        @SuppressWarnings("null")
        @Override
        public boolean addAll(Collection<? extends T> c) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (c.isEmpty()) {
                return false;
            }
            ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
            if (cell != null) {
                while (cell.next != cellAfter) {
                    cell = cell.next;
                }
            }
            for (T newElem : c) {
                ListCell<T> newCell = new ListCell<>();
                newCell.data = newElem;
                if (cell == null) {
                    head = newCell;
                } else {
                    newCell.next = cell.next;
                    cell.next = newCell;
                }
                cell = newCell;
            }
            if (cell.next == null) {
                tail = cell;
            }
            incrementLength(c.size());
            return true;
        }

        /**
         * @see java.util.List#addAll(int, java.util.Collection)
         */
        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0 || index > size()) {
                throw new IndexOutOfBoundsException(
                        "Index " + index + " should have been between 0 and size() (=" + size() + ')');
            }
            ListCell<T> cell = cellBefore;
            for (int i = 0; i < index; i++) {
                cell = cell.next;
            }
            for (T obj : c) {
                ListCell<T> newCell = new ListCell<>();
                newCell.data = obj;
                if (cell == null) {
                    head = newCell;
                } else {
                    newCell.next = cell.next;
                    cell.next = newCell;
                }
                cell = newCell;
            }
            if (cell.next == null) {
                tail = cell;
            }
            incrementLength(c.size());
            return true;
        }

        /**
         * @see java.util.List#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (sublistLength == 0 || c.isEmpty()) {
                return false;
            }
            int nrRemoved = 0;
            ListCell<T> previous = cellBefore;
            for (Object obj : c) {
                for (ListCell<T> cell = previous == null ? head : previous.next; cell != cellAfter; cell = cell.next) {
                    if (obj == null ? cell.data == null : obj.equals(cell.data)) {
                        if (previous == null) {
                            head = cell.next;
                        } else {
                            previous.next = cell.next;
                        }
                        nrRemoved++;
                    } else {
                        previous = cell;
                    }
                }
            }
            if (nrRemoved != 0) {
                if (previous == null) {
                    tail = null;
                } else if (previous.next == null) {
                    tail = previous;
                }
                incrementLength(-nrRemoved);
                return true;
            }
            return false;
        }

        /**
         * @see java.util.List#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll(Collection<?> c) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (sublistLength == 0) {
                return false;
            }
            int nrRemoved = 0;
            ListCell<T> previous = cellBefore;
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (!c.contains(cell.data)) {
                    if (previous == null) {
                        head = cell.next;
                    } else {
                        previous.next = cell.next;
                    }
                    nrRemoved++;
                } else {
                    previous = cell;
                }
            }
            if (nrRemoved == 0) {
                return false;
            }
            if (previous == null) {
                tail = null;
            } else if (previous.next == null) {
                tail = previous;
            }
            incrementLength(-nrRemoved);
            return true;
        }

        /**
         * @see java.util.List#clear()
         */
        @Override
        public void clear() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (sublistLength == 0) {
                return;
            }
            if (cellBefore == null) {
                head = cellAfter;
                if (head == null) {
                    tail = null;
                }
            } else {
                cellBefore.next = cellAfter;
            }
            incrementLength(-sublistLength);
        }

        /**
         * @see java.util.List#get(int)
         */
        @Override
        public T get(int index) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0 || index >= sublistLength) {
                throw new IndexOutOfBoundsException("index " + index + " should have been >= 0 and < " + sublistLength);
            }
            if (index == 0) {
                if (cellBefore == null) {
                    return head.data;
                }
                return cellBefore.next.data;
            }
            ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
            for (int i = 0; i < index; i++) {
                cell = cell.next;
            }
            return cell.data;
        }

        /**
         * @see java.util.List#set(int, java.lang.Object)
         */
        @Override
        public T set(int index, T element) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0 || index >= sublistLength) {
                throw new IndexOutOfBoundsException("index " + index + " should have been >= 0 and < " + sublistLength);
            }
            incrementLength(0);
            T result;
            if (index == 0) {
                if (cellBefore == null) {
                    result = head.data;
                    head.data = element;
                    return result;
                }
                result = cellBefore.next.data;
                cellBefore.next.data = element;
                return result;
            }
            ListCell<T> cell = cellBefore == null ? head : cellBefore.next;
            for (int i = 1; i < index; i++) {
                cell = cell.next;
            }
            result = cell.data;
            cell.data = element;
            return result;
        }

        /**
         * @see java.util.List#add(int, java.lang.Object)
         */
        @Override
        public void add(int index, T element) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0 || index >= sublistLength) {
                throw new IndexOutOfBoundsException(
                        "index " + index + " should have been >= 0 and <= " + sublistLength);
            }
            ListCell<T> newCell = new ListCell<>();
            newCell.data = element;
            if (index == 0) {
                if (cellBefore == null) {
                    if (head != null) {
                        newCell.next = head.next;
                    }
                    head = newCell;
                } else {
                    newCell.next = cellBefore.next;
                    cellBefore.next = newCell;
                }
            } else {
                ListCell<T> previous = cellBefore == null ? head : cellBefore.next;
                for (int i = 1; i < index; i++) {
                    previous = previous.next;
                }
                newCell.next = previous.next;
                previous.next = newCell;
            }
            if (newCell.next == null) {
                tail = newCell;
            }
            incrementLength(1);
        }

        /**
         * @see java.util.List#remove(int)
         */
        @Override
        public T remove(int index) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (index < 0 || index >= sublistLength) {
                throw new IndexOutOfBoundsException("index " + index + " should have been >= 0 and < " + sublistLength);
            }
            ListCell<T> zombie;
            if (index == 0) {
                if (cellBefore == null) {
                    zombie = head;
                    head = head.next;
                    if (head == null) {
                        tail = null;
                    } else if (head.next == null) {
                        tail = head;
                    }
                } else {
                    zombie = cellBefore.next;
                    cellBefore.next = zombie.next;
                    if (cellBefore.next == null) {
                        tail = cellBefore;
                    }
                }
            } else {
                ListCell<T> previous = cellBefore == null ? head : cellBefore.next;
                for (int i = 1; i < index; i++) {
                    previous = previous.next;
                }
                zombie = previous.next;
                previous.next = zombie.next;
                if (previous.next == null) {
                    tail = previous;
                }
            }
            incrementLength(-1);
            return zombie.data;
        }

        /**
         * @see java.util.List#indexOf(java.lang.Object)
         */
        @Override
        public int indexOf(Object o) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            int i = 0;
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (o == null ? cell.data == null : o.equals(cell.data)) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        /**
         * @see java.util.List#lastIndexOf(java.lang.Object)
         *
         * @param o
         * @return
         */
        @Override
        public int lastIndexOf(Object o) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            int result = -1;
            int i = 0;
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (o == null ? cell.data == null : o.equals(cell.data)) {
                    result = i;
                }
                i++;
            }
            return result;
        }

        /**
         * @see java.util.List#listIterator()
         *
         * @return
         */
        @Override
        public ListIterator<T> listIterator() {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return new InternalSublistIterator(0);
        }

        /**
         * @see java.util.List#listIterator(int)
         *
         * @param index
         * @return
         */
        @Override
        public ListIterator<T> listIterator(int index) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            return new InternalSublistIterator(index);
        }

        /**
         * @see java.util.List#subList(int, int)
         */
        @Override
        public java.util.List<T> subList(int subFromIndex, int subToIndex) {
            if (atUpdateCount != updateCount) {
                throw new ConcurrentModificationException();
            }
            if (subFromIndex < 0 || subToIndex > sublistLength || subFromIndex > subToIndex) {
                throw new IndexOutOfBoundsException();
            }
            return new Sublist(subFromIndex + fromIndex, subToIndex + fromIndex);
        }

        @Override
        public String toString() {
            if (sublistLength == 0) {
                return "()";
            }
            StringBuilder result = new StringBuilder();
            String separator = "(";

            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                result.append(separator).append(cell.data);
                separator = ", ";

            }
            result.append(')');
            return result.toString();
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int hashCode = 1;
            for (ListCell<T> cell = cellBefore == null ? head : cellBefore.next; cell != cellAfter; cell = cell.next) {
                if (cell.data != null) {
                    hashCode = 31 * hashCode + (cell.data == null ? 0 : cell.data.hashCode());
                }
            }
            return hashCode;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof java.util.List)) {
                return false;
            }
            if (length != ((java.util.List<?>) obj).size()) {
                return false;
            }

            Iterator<Object> it = ((java.util.List<Object>) obj).iterator();
            for (ListCell<T> cell = head; cell != null; cell = cell.next) {
                if (!Objects.deepEquals(cell.data, it.next())) {
                    return false;
                }
            }
            return true;
        }

        protected int incrementLength(int count) {
            sublistLength += count;
            List.this.length += count;
            atUpdateCount = ++updateCount;
            return atUpdateCount;
        }

        private class InternalSublistIterator implements ListIterator<T> {
            private ListCell<T> cell;
            private int index = -1;
            private int cursor;

            private int iteratorAtUpdateCount;

            /**
             * Constructor
             */
            public InternalSublistIterator(int ind) {
                if (ind < 0 || ind > sublistLength) {
                    throw new IndexOutOfBoundsException("Index " + ind + " should be >= 0 and <= " + sublistLength);
                }
                iteratorAtUpdateCount = updateCount;
                while (index < ind - 1) {
                    next();
                }
            }

            /**
             * @see java.util.ListTerator#hasNext()
             */
            @Override
            public boolean hasNext() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                return cursor < sublistLength;
            }

            /**
             * @see java.util.ListTerator#next()
             */
            @Override
            public T next() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                if (cursor == index) {
                    cursor++;
                    return cell.data;
                }
                if (cursor < sublistLength) {
                    index = cursor;
                    if (index == 0) {
                        cell = cellBefore == null ? head : cellBefore.next;
                    } else {
                        cell = cell.next;
                    }
                    cursor++;
                    return cell.data;
                }
                throw new NoSuchElementException();
            }

            /**
             * @see java.util.ListTerator#hasPrevious()
             */
            @Override
            public boolean hasPrevious() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                return cursor != 0;
            }

            /**
             * @see java.util.ListTerator#previous()
             */
            @Override
            public T previous() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                if (cursor <= 0) {
                    throw new NoSuchElementException();
                }
                index = cursor - 1;
                cell = cellBefore == null ? head : cellBefore.next;
                for (int i = 0; i < index; i++) {
                    cell = cell.next;
                }
                cursor = index;
                return cell.data;
            }

            /**
             * @see java.util.ListTerator#nextIndex()
             */
            @Override
            public int nextIndex() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                return cursor;
            }

            /**
             * @see java.util.ListTerator#previousIndex()
             */
            @Override
            public int previousIndex() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                return cursor - 1;
            }

            /**
             * @see java.util.ListTerator#remove()
             */
            @Override
            public void remove() {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                if (index < 0) {
                    throw new IllegalStateException();
                }
                if (index == 0) {
                    if (cellBefore == null) {
                        head = head.next;
                        if (head == null) {
                            tail = null;
                        }
                    } else {
                        cellBefore.next = cellBefore.next.next;
                        if (cellBefore.next == null) {
                            tail = cellBefore;
                        }
                    }
                } else {
                    ListCell<T> toBeRemoved = cellBefore == null ? head : cellBefore.next;
                    for (int i = 0; i < index; i++) {
                        cell = toBeRemoved;
                        toBeRemoved = cell.next;
                    }
                    cell.next = toBeRemoved.next;
                    if (cell.next == null) {
                        tail = cell;
                    }
                }
                cursor = index;
                iteratorAtUpdateCount = incrementLength(-1);
                index = -1;
            }

            /**
             * @see java.util.ListTerator#set(java.lang.Object)
             */
            @Override
            public void set(T e) {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                if (index < 0) {
                    throw new IllegalStateException();
                }
                if (index == 0) {
                    cell = head;
                }
                cell.data = e;
                iteratorAtUpdateCount = incrementLength(0);
            }

            /**
             * @see java.util.ListTerator#add(java.lang.Object)
             */
            @Override
            public void add(T e) {
                if (iteratorAtUpdateCount != updateCount) {
                    throw new ConcurrentModificationException();
                }
                ListCell<T> newCell = new ListCell<>();
                newCell.data = e;
                if (cursor == 0) {
                    if (cellBefore == null) {
                        newCell.next = head;
                        head = newCell;
                    } else {
                        newCell.next = cellBefore.next;
                        cellBefore.next = newCell;
                    }
                } else {
                    cell = cellBefore == null ? head : cellBefore.next;
                    for (int i = 1; i < cursor; i++) {
                        cell = cell.next;
                    }
                    newCell.next = cell.next;
                    cell.next = newCell;
                }
                if (newCell.next == null) {
                    tail = newCell;
                }
                cell = newCell;
                iteratorAtUpdateCount = incrementLength(1);
                index = -1;
            }
        }
    }
}
