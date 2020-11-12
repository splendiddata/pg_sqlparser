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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.4.1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class AlterTableMoveAllStmt extends Node {
    @XmlAttribute
    public String orig_tablespacename;

    /** Object type to move */
    @XmlAttribute
    public ObjectType objtype;

    /** List of roles to move objects of */
    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public List<Node> roles;

    @XmlAttribute
    public String new_tablespacename;

    @XmlAttribute
    public boolean nowait;

    /**
     * Constructor
     */
    public AlterTableMoveAllStmt() {
        super(NodeTag.T_AlterTableMoveAllStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterTableMoveAllStmt to copy
     */
    public AlterTableMoveAllStmt(AlterTableMoveAllStmt original) {
        super(original);
        this.orig_tablespacename = original.orig_tablespacename;
        this.objtype = original.objtype;
        if (original.roles != null) {
            this.roles = original.roles.clone();
        }
        this.new_tablespacename = original.new_tablespacename;
        this.nowait = original.nowait;
    }

    @Override
    public AlterTableMoveAllStmt clone() {
        AlterTableMoveAllStmt clone = (AlterTableMoveAllStmt) super.clone();
        if (this.roles != null) {
            clone.roles = this.roles.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(objtype).append(" all in tablespace ")
                .append(ParserUtil.identifierToSql(orig_tablespacename));

        if (roles != null) {
            String separator = " owned by ";
            for (Node role : roles) {
                result.append(separator).append(ParserUtil.identifierToSql(role.toString()));
                separator = ", ";
            }
        }

        result.append(" set tablespace ").append(ParserUtil.identifierToSql(new_tablespacename));

        if (nowait) {
            result.append(" nowait");
        }

        return result.toString();
    }
}
