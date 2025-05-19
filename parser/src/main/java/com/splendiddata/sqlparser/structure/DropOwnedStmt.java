/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DropOwnedStmt extends Node {
    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public List<RoleSpec> roles;

    @XmlAttribute
    public DropBehavior behavior;

    /**
     * Constructor
     */
    public DropOwnedStmt() {
        super(NodeTag.T_DropOwnedStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DropOwnedStmt to copy
     */
    public DropOwnedStmt(DropOwnedStmt original) {
        super(original);
        if (original.roles != null) {
            this.roles = original.roles.clone();
        }
        this.behavior = original.behavior;
    }

    @Override
    public DropOwnedStmt clone() {
        DropOwnedStmt clone = (DropOwnedStmt) super.clone();
        if (roles != null) {
            clone.roles = roles.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String separator = "drop owned by ";
        for (RoleSpec role : roles) {
            result.append(separator).append(role);
            separator = ", ";
        }

        result.append(behavior);

        return result.toString();
    }
}
