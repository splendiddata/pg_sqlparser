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

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The postition of the item in the scanner input.
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class Position implements Cloneable, Comparable<Position> {

    private final long offset;

    public Position() {
        this(0);
    }

    /**
     * Constructor
     *
     * @param offset
     *            The offset from the start of the input (file / String / ... whatever the Reader reads)
     */
    public Position(long offset) {
        this.offset = offset;
    }

    /**
     * @return long the offset
     */
    public final long getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "" + offset;
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("com.splendiddata.sqlparser.structure.Position.clone()->failed", e);
        }
    }

    @Override
    public int compareTo(Position o) {
        if (o == null) {
            return 1;
        }
        return Long.compare(offset, o.offset);
    }
}
