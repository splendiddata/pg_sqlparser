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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.SQLValueFunctionOp;

/**
 * copied from: /postgresql-10rc1/src/include/nodes/primnodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class SQLValueFunction extends Expr {

    /** which function this is */
    @XmlAttribute
    public SQLValueFunctionOp op;

    @XmlAttribute
    public int typmod;

    /**
     * Constructor
     */
    public SQLValueFunction() {
        super(NodeTag.T_SQLValueFunction);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The SQLValueFunction to copy
     */
    public SQLValueFunction(SQLValueFunction original) {
        super(original);
        this.op = original.op;
        this.typmod = original.typmod;
    }

    @Override
    public SQLValueFunction clone() {
        return (SQLValueFunction) super.clone();
    }

    @Override
    public String toString() {
        if (op != null) {
            switch (op) {
            case SVFOP_CURRENT_TIMESTAMP_N:
            case SVFOP_CURRENT_TIME_N:
            case SVFOP_LOCALTIMESTAMP_N:
            case SVFOP_LOCALTIME_N:
                return new StringBuilder().append(op.toString()).append('(').append(typmod).append(')').toString();
            default:
                return op.toString();
            }
        }
        return "Empty " + getClass().getName();
    }
}
