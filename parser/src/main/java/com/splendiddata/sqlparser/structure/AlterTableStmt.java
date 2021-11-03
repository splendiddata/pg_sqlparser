/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2021
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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterTableStmt extends Node {
    /** table to work on */
    @XmlElement
    public RangeVar relation;

    /** list of subcommands */
    @XmlElementWrapper(name = "cmds")
    @XmlElement(name = "cmd_name")
    public List<AlterTableCmd> cmds;

    /** type of object */
    @XmlAttribute
    public ObjectType relkind;

    /**
     * type of object
     * 
     * @since Postgres version 14.
     * @deprecated Implemented for forward compatibility with Postgres 14 
     */
    @XmlTransient
    @Deprecated
    public ObjectType objtype;

    /** skip error if table missing */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterTableStmt() {
        super(NodeTag.T_AlterTableStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterTableStmt to copy
     */
    public AlterTableStmt(AlterTableStmt original) {
        super(original);
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.cmds != null) {
            this.cmds = original.cmds.clone();
        }
        this.relkind = original.relkind;
        this.missing_ok = original.missing_ok;
    }

    @Override
    public AlterTableStmt clone() {
        AlterTableStmt clone = (AlterTableStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (cmds != null) {
            clone.cmds = cmds.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(relkind);

        if (missing_ok) {
            result.append(" if exists");
        }

        if (relation != null) {
            result.append(' ').append(relation);
        }

        String tableOrAttribute = ObjectType.OBJECT_TYPE.equals(relkind) ? "attribute" : "column";
        String separator = " ";
        for (AlterTableCmd cmd : cmds) {
            result.append(separator).append(cmd.toString(tableOrAttribute));
            separator = ", ";
        }

        return result.toString();
    }
}
