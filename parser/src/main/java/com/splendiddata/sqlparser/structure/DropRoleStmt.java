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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DropRoleStmt extends Node {
    /** List of roles to remove */
    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public List<RoleSpec> roles;

    /** skip error if a role is missing? */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public DropRoleStmt() {
        super(NodeTag.T_DropRoleStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DropRoleStmt to copy
     */
    public DropRoleStmt(DropRoleStmt original) {
        super(original);
        if (original.roles != null) {
            this.roles = original.roles.clone();
        }
        this.missing_ok = original.missing_ok;
    }

    @Override
    public DropRoleStmt clone() {
        DropRoleStmt clone = (DropRoleStmt) super.clone();
        if (roles != null) {
            clone.roles = roles.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("drop role ");

        if (missing_ok) {
            result.append("if exists ");
        }

        String separator = "";
        for (RoleSpec role : roles) {
            result.append(separator).append(role);
            separator = ", ";
        }

        return result.toString();
    }
}
