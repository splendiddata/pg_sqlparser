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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterRoleSetStmt extends Node {
    /** role name */
    @XmlElement
    public Node role;

    /** database name, or NULL */
    @XmlAttribute
    public String database;

    /** SET or RESET subcommand */
    @XmlElement
    public VariableSetStmt setstmt;

    /**
     * Constructor
     */
    public AlterRoleSetStmt() {
        super(NodeTag.T_AlterRoleSetStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterRoleSetStmt to copy
     */
    public AlterRoleSetStmt(AlterRoleSetStmt original) {
        super(original);
        if (original.role != null) {
            this.role = original.role.clone();
        }
        this.database = original.database;
        if (original.setstmt != null) {
            this.setstmt = original.setstmt.clone();
        }
    }

    @Override
    public AlterRoleSetStmt clone() {
        AlterRoleSetStmt clone = (AlterRoleSetStmt) super.clone();
        if (role != null) {
            clone.role = role.clone();
        }
        if (setstmt != null) {
            clone.setstmt = setstmt.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter role ");
        if (role == null) {
            result.append("all");
        } else {
            result.append(role);
        }

        if (database != null) {
            result.append(" in database ").append(ParserUtil.identifierToSql(database));
        }

        if (setstmt != null) {
            result.append(' ').append(setstmt);
        }

        return result.toString();
    }
}
