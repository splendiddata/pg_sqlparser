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
 * A case expression, copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class TypeCast extends Expr {

    /** the expression being casted */
    @XmlElement
    public Node arg;

    /** the target type */
    @XmlElement
    public TypeName typeName;

    public TypeCast() {
        super(NodeTag.T_TypeCast);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The TypeCast to copy
     */
    public TypeCast(TypeCast original) {
        super(original);
        this.typeName = original.typeName.clone();
        this.arg = original.arg.clone();
    }

    @Override
    public String toString() {
        if (arg != null && typeName != null && typeName.names.size() == 2
                && "pg_catalog".equals(typeName.names.get(0).val.str)) {
            if ("bool".equals(typeName.names.get(1).val.str)) {
                switch (arg.toString()) {
                case "'t'":
                    return "true";
                case "'f'":
                    return "false";
                default:
                    break;
                }
            }
            if ("interval".equals(typeName.names.get(1).val.str)) {
                StringBuilder result = new StringBuilder();
                switch (arg.type) {
                case T_A_Const:
                case T_ColumnRef:
                case T_TypeCast:
                    result.append(arg);
                    break;
                default:
                    result.append('(').append(arg).append(')');
                    break;
                }
                result.append("::").append(typeName);
                return result.toString();
            }
        }

        if (arg instanceof A_Expr) {
            return new StringBuilder().append("cast(").append(arg).append(" as ").append(typeName).append(')')
                    .toString();
        }

        if (arg instanceof A_Const) {
            if (NodeTag.T_String.equals(((A_Const) arg).val.type) && "char".equals(typeName.toString())) {
                return new StringBuilder().append(arg).append("::").append("\"char\"").toString();
            }
            if (NodeTag.T_Integer.equals(((A_Const) arg).val.type) && ((A_Const) arg).val.val.ival < 0) {
                return new StringBuilder().append('(').append(arg).append(')').append("::").append(typeName).toString();
            }
        }

        return new StringBuilder().append(arg).append("::").append(typeName).toString();
    }

    @Override
    public TypeCast clone() {
        TypeCast clone = (TypeCast) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        return clone;
    }
}
