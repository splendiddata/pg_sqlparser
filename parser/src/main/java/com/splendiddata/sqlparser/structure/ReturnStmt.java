/*
 * Copyright (c) Splendid Data Product Development B.V. 2021
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * RETURN statement (inside SQL function body)
 * <p>
 * Copied from /postgresql-14beta2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ReturnStmt extends Node {

    @XmlElement
    public Node returnval;

    /**
     * Constructor
     */
    public ReturnStmt() {
        super(NodeTag.T_ReturnStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            ReturnStmt to copy
     */
    public ReturnStmt(ReturnStmt original) {
        super(original);
        if (original.returnval != null) {
            this.returnval = original.returnval.clone();
        }
    }

    @Override
    public ReturnStmt clone() {
        ReturnStmt clone = (ReturnStmt) super.clone();
        if (returnval != null) {
            clone.returnval = returnval.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "return " + returnval;
    }
}
