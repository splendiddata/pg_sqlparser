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

import com.splendiddata.sqlparser.enums.CoercionForm;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class RowExpr extends Expr {

    /** the fields */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /** RECORDOID or a composite type's ID */
    @XmlElement
    public Oid row_typeid;

    /** how to display this node */
    @XmlAttribute
    public CoercionForm row_format;

    /** list of String, or NIL */
    @XmlElementWrapper(name = "colnames")
    @XmlElement(name = "colname")
    public List<Value> colnames;

    /**
     * Constructor
     */
    public RowExpr() {
        super(NodeTag.T_RowExpr);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The RowExpr to copy
     */
    public RowExpr(RowExpr toCopy) {
        super(toCopy);
        if (toCopy.args != null) {
            this.args = toCopy.args.clone();
        }
        this.row_typeid = toCopy.row_typeid;
        this.row_format = toCopy.row_format;
        if (toCopy.colnames != null) {
            this.colnames = toCopy.colnames.clone();
        }
    }

    @Override
    public String toString() {
        if (args == null) {
            return "row()";
        }

        String separator = "";
        StringBuilder result = new StringBuilder("row(");
        for (Node node : args) {
            result.append(separator).append(node);
            separator = ", ";
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public RowExpr clone() {
        RowExpr clone = (RowExpr) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        if (row_typeid != null) {
            clone.row_typeid = row_typeid.clone();
        }
        if (colnames != null) {
            clone.colnames = colnames.clone();
        }
        return clone;
    }
}
