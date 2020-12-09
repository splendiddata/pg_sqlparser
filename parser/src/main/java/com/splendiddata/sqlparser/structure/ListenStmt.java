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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ListenStmt extends Node {
    /** condition name to listen on */
    @XmlAttribute
    public String conditionname;

    /**
     * Constructor
     *
     */
    public ListenStmt() {
        super(NodeTag.T_ListenStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ListenStmt to copy
     */
    public ListenStmt(ListenStmt original) {
        super(original);
        this.conditionname = original.conditionname;
    }

    @Override
    public ListenStmt clone() {
        return (ListenStmt) super.clone();
    }

    @Override
    public String toString() {
        return "listen " + ParserUtil.identifierToSql(conditionname);
    }
}
