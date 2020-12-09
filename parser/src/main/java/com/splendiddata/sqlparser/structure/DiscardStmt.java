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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.DiscardMode;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DiscardStmt extends Node {
    @XmlAttribute
    public DiscardMode target;

    /**
     * Constructor
     */
    public DiscardStmt() {
        super(NodeTag.T_DiscardStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DiscardStmt to copy
     */
    public DiscardStmt(DiscardStmt original) {
        this.target = original.target;
    }

    @Override
    public DiscardStmt clone() {
        return (DiscardStmt) super.clone();
    }

    @Override
    public String toString() {
        return "discard " + target;
    }
}
