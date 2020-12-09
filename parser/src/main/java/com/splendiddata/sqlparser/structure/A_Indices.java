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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class A_Indices extends Node {
    /** NULL if it's a single subscript */
    @XmlElement
    public Node lidx;

    @XmlElement
    public Node uidx;

    /** true if slice (i.e., colon is present). */
    @XmlElement
    public boolean is_slice;

    /**
     * Constructor
     */
    public A_Indices() {
        super(NodeTag.T_A_Indices);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The A_Indices to copy
     */
    public A_Indices(A_Indices toCopy) {
        super(toCopy);
        if (toCopy.lidx != null) {
            this.lidx = toCopy.lidx.clone();
        }
        if (toCopy.uidx != null) {
            this.uidx = toCopy.uidx.clone();
        }
        this.is_slice = toCopy.is_slice;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append('[');
        if (lidx != null) {
            result.append(lidx).append(':');
        }
        result.append(uidx);
        result.append(']');
        return result.toString();
    }

    @Override
    public A_Indices clone() {
        A_Indices clone = (A_Indices) super.clone();
        if (lidx != null) {
            clone.lidx = lidx.clone();
        }
        if (uidx != null) {
            clone.uidx = uidx.clone();
        }
        return clone;
    }
}
