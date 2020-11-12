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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class A_Indirection extends Expr {
    /**
     * the thing being selected from
     */
    @XmlElement
    public Node arg;

    /**
     * subscripts and/or field names and/or *
     */
    @XmlElementWrapper(name = "indirection")
    @XmlElement(name = "subscriptOrfieldName")
    public List<Node> indirection;

    /**
     * Constructor
     */
    public A_Indirection() {
        super(NodeTag.T_A_Indirection);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The A_Indirection to copy
     */
    public A_Indirection(A_Indirection toCopy) {
        super(toCopy);
        if (toCopy.arg != null) {
            this.arg = toCopy.arg.clone();
        }
        if (toCopy.indirection != null) {
            this.indirection = toCopy.indirection.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append('(').append(arg).append(')');

        for (Node ind : indirection) {
            if (ind instanceof Value) {
                result.append('.').append(((Value) ind).val.str);
            } else if (ind instanceof A_Star) {
                result.append(".*");
            } else {
                result.append(ind);
            }
        }

        return result.toString();
    }

    @Override
    public A_Indirection clone() {
        A_Indirection clone = (A_Indirection) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        if (indirection != null) {
            clone.indirection = indirection.clone();
        }
        return clone;
    }
}
