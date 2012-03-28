/**
 * Copyright (c) 2011 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Jan 6, 2011
 */
package jndi.view.utils;

import java.util.Iterator;

/**
 * @param <T>
 *        a type
 * @author Alistair A. Israel
 */
public abstract class ReadOnlyIterator<T> implements Iterator<T> {

    /**
     * {@inheritDoc}
     *
     * @see java.util.Iterator#remove()
     */
    public final void remove() {
        throw new UnsupportedOperationException("remove() called on read-only iterator!");
    }

}
