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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * A constant value. Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class A_Const extends Expr {

    /** value (includes type info, see value.h) */
    @XmlElement
    public Value val = new Value();

    /**
     * Constructor
     */
    public A_Const() {
        super();
        type = NodeTag.T_A_Const;
    }

    /**
     * Copy constructor
     *
     * @param toCopy The constant to copy
     */
    public A_Const(A_Const toCopy) {
        super(toCopy);
        val = new Value(toCopy.val);
    }

    @Override
    public String toString() {
        switch (val.type) {
        case T_String:
            return ParserUtil.toSqlTextString(val.toString());
        case T_Integer:
            return val.toString();
        case T_Float:
            return val.toString();
        case T_Null:
            return "null";
        case T_BitString:
            return "x'" + val.toString().substring(1) + '\'';
        default:
            return "??????? Unknown A_Const type: " + val.type + ", value = " + val.toString();
        }
    }

    @Override
    public A_Const clone() {
        A_Const clone = (A_Const) super.clone();
        clone.val = val.clone();
        return clone;
    }
}
