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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * MultiAssignRef - element of a row source expression for UPDATE
 * <p>
 * In an UPDATE target list, when we have SET (a,b,c) = row-valued-expression, we generate separate ResTarget items for
 * each of a,b,c. Their "val" trees are MultiAssignRef nodes numbered 1..n, linking to a common copy of the
 * row-valued-expression (which parse analysis will process only once, when handling the MultiAssignRef with colno=1).
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class MultiAssignRef extends Node {
    /** the row-valued expression */
    @XmlElement
    public Node source;

    /** column number for this target (1..n) */
    @XmlAttribute
    public int colno;

    /** number of targets in the construct */
    @XmlAttribute
    public int ncolumns;

    /**
     * Constructor
     */
    public MultiAssignRef() {
        super(NodeTag.T_MultiAssignRef);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public MultiAssignRef(MultiAssignRef original) {
        super(original);
        if (original.source != null) {
            this.source = original.source.clone();
        }
        this.colno = original.colno;
        this.ncolumns = original.ncolumns;
    }

    @Override
    public MultiAssignRef clone() {
        MultiAssignRef clone = (MultiAssignRef) super.clone();
        if (source != null) {
            clone.source = source.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return source.toString();
    }
}
