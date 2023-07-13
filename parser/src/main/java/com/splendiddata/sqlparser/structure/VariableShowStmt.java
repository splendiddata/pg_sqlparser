/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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
public class VariableShowStmt extends Node {
    @XmlAttribute
    public String name;

    /**
     * Constructor
     */
    public VariableShowStmt() {
        super(NodeTag.T_VariableShowStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The VariableShowStmt to copy
     */
    public VariableShowStmt(VariableShowStmt original) {
        super(original);
        this.name = original.name;
    }

    @Override
    public VariableShowStmt clone() {
        return (VariableShowStmt) super.clone();
    }

    @Override
    public String toString() {
        if (!name.contains(".")) {
            return "show " + ParserUtil.identifierToSql(name);
        }
        StringBuilder result = new StringBuilder("show");
        char delimiter = ' ';
        for (String part : name.split("\\.")) {
            result.append(delimiter).append(ParserUtil.identifierToSql(part));
            delimiter = '.';
        }
        return result.toString();
    }
}
