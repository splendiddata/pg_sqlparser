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
public class A_ArrayExpr extends Expr {
    /** array element expressions */
    @XmlElementWrapper(name = "elements")
    @XmlElement(name = "element")
    public List<Node> elements;

    /**
     * Constructor
     */
    public A_ArrayExpr() {
        super(NodeTag.T_A_ArrayExpr);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The expression to copy
     */
    public A_ArrayExpr(A_ArrayExpr toCopy) {
        super(toCopy);
        if (toCopy.elements != null) {
            this.elements = toCopy.elements.clone();
        }
    }

    @Override
    public String toString() {
        if (elements == null) {
            return "array[]";
        }
        String separator = "";
        StringBuilder result = new StringBuilder("array[");
        for (Node element : elements) {
            result.append(separator).append(element);
            separator = ", ";
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public A_ArrayExpr clone() {
        A_ArrayExpr clone = (A_ArrayExpr) super.clone();
        if (elements != null) {
            clone.elements = elements.clone();
        }
        return clone;
    }
}
