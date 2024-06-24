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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ResTarget extends Expr {

    /** column name or NULL */
    @XmlAttribute
    public String name;

    /** subscripts, field names, and '*', or NIL */
    @XmlElementWrapper(name = "indirection")
    @XmlElement(name = "indirectionNode")
    public List<Node> indirection;

    /** the value expression to compute or assign */
    @XmlElement
    public Node val;

    /**
     * Constructor
     */
    public ResTarget() {
        super();
        type = NodeTag.T_ResTarget;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The ResTarget to copy
     */
    public ResTarget(ResTarget other) {
        super(other);
        this.name = other.name;
        this.indirection = other.indirection;
        this.val = other.val;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String leadingSpace = "";
        if (val != null) {
            result.append(leadingSpace).append(val);
            leadingSpace = " ";
        }
        if (name != null) {
            if (result.length() != 0) {
                result.append(" as ");
            }
            if ("*".equals(name)) {
                result.append("\"*\"");
            } else {
                result.append(ParserUtil.identifierToSql(name));
            }
            leadingSpace = " ";
        }
        if (indirection != null) {
            leadingSpace = " ";
            for (Object ind : indirection) {
                if (!(ind instanceof A_Indices)) {
                    result.append('.');
                }
                result.append(ind);
            }

        }
        if ("".equals(leadingSpace)) {
            result.append("???");
        }
        return result.toString();
    }

    @Override
    public ResTarget clone() {
        ResTarget clone = (ResTarget) super.clone();
        if (indirection != null) {
            clone.indirection = indirection.clone();
        }
        if (val != null) {
            clone.val = val.clone();
        }
        return clone;
    }
}
