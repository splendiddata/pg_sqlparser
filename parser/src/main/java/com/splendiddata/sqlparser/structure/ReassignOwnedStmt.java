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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * REASSIGN OWNED statement
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class ReassignOwnedStmt extends Node {
    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public List<RoleSpec> roles;

    @XmlElement
    public Node newrole;

    /**
     * Constructor
     */
    public ReassignOwnedStmt() {
        super(NodeTag.T_ReassignOwnedStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ReassignOwnedStmt to copy
     */
    public ReassignOwnedStmt(ReassignOwnedStmt original) {
        super(original);
        if (original.roles != null) {
            this.roles = original.roles.clone();
        }
        if (original.newrole != null) {
            this.newrole = original.newrole.clone();
        }
    }

    @Override
    public ReassignOwnedStmt clone() {
        ReassignOwnedStmt clone = (ReassignOwnedStmt) super.clone();
        if (roles != null) {
            clone.roles = roles.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("reassign owned by");

        String separator = " ";
        for (RoleSpec role : roles) {
            result.append(separator).append(role);
            separator = ", ";
        }

        result.append(" to ").append(newrole);

        return result.toString();
    }
}
