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

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.NullTestType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h, stripped from unused fields
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class NullTest extends Expr {
    /** input expression */
    @XmlElement
    public Expr arg;

    /** IS NULL, IS NOT NULL */
    @XmlAttribute
    public NullTestType nulltesttype;

    /**
     * Constructor
     */
    public NullTest() {
        super(NodeTag.T_NullTest);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The NullTest to copy
     */
    public NullTest(NullTest toCopy) {
        super(toCopy);
        if (toCopy.arg != null) {
            this.arg = toCopy.arg.clone();
        }
        this.nulltesttype = toCopy.nulltesttype;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (arg instanceof A_Expr) {
            result.append('(').append(arg).append(')');
        } else {
            result.append(arg);
        }
        switch (nulltesttype) {
        case IS_NOT_NULL:
            result.append(" is not null");
            break;
        case IS_NULL:
            result.append(" is null");
            break;
        default:
            throw new AssertionError("Unsipported NullTestType: " + nulltesttype);
        }

        return result.toString();
    }

    @Override
    public NullTest clone() {
        NullTest clone = (NullTest) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        return clone;
    }
}
