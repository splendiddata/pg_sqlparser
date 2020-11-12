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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.4.1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class AlterSystemStmt extends Node {
    /* SET subcommand */
    @XmlElement
    public VariableSetStmt setstmt;

    /**
     * Constructor
     */
    public AlterSystemStmt() {
        super(NodeTag.T_AlterSystemStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterSystemStmt to copy
     */
    public AlterSystemStmt(AlterSystemStmt original) {
        super(original);
        if (original.setstmt != null) {
            this.setstmt = original.setstmt.clone();
        }
    }

    @Override
    public AlterSystemStmt clone() {
        AlterSystemStmt clone = (AlterSystemStmt) super.clone();
        if (this.setstmt != null) {
            clone.setstmt = this.setstmt.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "alter system " + setstmt;
    }
}
