/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DeallocateStmt extends Node {
    /** The name of the plan to remove */
    @XmlAttribute
    public String name;

    /**
     * True if DEALLOCATE ALL. This is redundant with "name == NULL", but we make it a separate field so that exactly
     * this condition (and not the precise name) will be accounted for in query jumbling.
     * 
     * @since Postgres 17
     */
    @XmlAttribute
    public boolean isall;

    /**
     * Constructor
     */
    public DeallocateStmt() {
        super(NodeTag.T_DeallocateStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DeallocateStmt to copy
     */
    public DeallocateStmt(DeallocateStmt original) {
        super(original);
        this.name = original.name;
        this.isall = original.isall;
    }

    @Override
    public DeallocateStmt clone() {
        return (DeallocateStmt) super.clone();
    }

    @Override
    public String toString() {
        if (name == null) {
            return "deallocate all";
        }
        return "deallocate " + ParserUtil.identifierToSql(name);
    }
}
