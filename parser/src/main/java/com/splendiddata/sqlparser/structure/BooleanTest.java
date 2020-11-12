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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.BoolTestType;

/**
 * Boolean expression, copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class BooleanTest extends Expr {

    /** input expression */
    @XmlElement
    public Expr arg;

    /** test type */
    @XmlAttribute
    public BoolTestType booltesttype;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (arg instanceof A_Expr) {
            result.append('(').append(arg).append(')');
        } else {
            result.append(arg);
        }

        switch (booltesttype) {
        case IS_FALSE:
            result.append(" is false");
            break;
        case IS_NOT_FALSE:
            result.append(" is not false");
            break;
        case IS_NOT_TRUE:
            result.append(" is not true");
            break;
        case IS_NOT_UNKNOWN:
            result.append(" is not unknown");
            break;
        case IS_TRUE:
            result.append(" is true");
            break;
        case IS_UNKNOWN:
            result.append(" is unknown");
            break;
        default:
            throw new AssertionError("Unimplemented BoolTestType: " + booltesttype);
        }

        return result.toString();
    }

    @Override
    public BooleanTest clone() {
        BooleanTest clone = (BooleanTest) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        return clone;
    }
}
