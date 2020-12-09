/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at Client's option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, Client should
 * obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from postgresql-11beta4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 6.0
 */
@XmlRootElement(namespace = "parser")
public class CallStmt extends Node {
    /** from the parser */
    @XmlElement
    public FuncCall funccall;

    /**
     * Constructor
     */
    public CallStmt() {
        super(NodeTag.T_CallStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CallStmt to copy
     */
    public CallStmt(CallStmt original) {
        super(original);
        if (original.funccall != null) {
            this.funccall = original.funccall.clone();
        }
    }

    @Override
    public CallStmt clone() {
        CallStmt clone = (CallStmt) super.clone();
        if (funccall != null) {
            clone.funccall = funccall.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "call " + funccall;
    }
}
