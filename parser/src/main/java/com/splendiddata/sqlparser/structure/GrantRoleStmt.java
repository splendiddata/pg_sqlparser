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
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class GrantRoleStmt extends Node {

    /** list of roles to be granted/revoked */
    @XmlElementWrapper(name = "grant_roles")
    @XmlElement(name = "grant_role")
    public List<AccessPriv> granted_roles;

    /** list of member roles to add/delete */
    @XmlElementWrapper(name = "grantee_roles")
    @XmlElement(name = "grantee_role")
    public List<RoleSpec> grantee_roles;

    /** true = GRANT, false = REVOKE */
    @XmlAttribute
    public boolean is_grant;

    /** with admin option */
    @XmlAttribute
    public boolean admin_opt;

    /** set grantor to other than current role */
    @XmlElement
    public Node grantor;

    /** drop behavior (for REVOKE) */
    @XmlAttribute
    public DropBehavior behavior;

    /**
     * Constructor
     */
    public GrantRoleStmt() {
        super(NodeTag.T_GrantRoleStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The GrantRoleStmt to copy
     */
    public GrantRoleStmt(GrantRoleStmt original) {
        super(original);
        if (original.granted_roles != null) {
            this.granted_roles = original.granted_roles.clone();
        }
        if (original.grantee_roles != null) {
            this.grantee_roles = original.grantee_roles.clone();
        }
        this.is_grant = original.is_grant;
        this.admin_opt = original.admin_opt;
        if (original.grantor != null) {
            this.grantor = original.grantor.clone();
        }
        this.behavior = original.behavior;

    }

    @Override
    public GrantRoleStmt clone() {
        GrantRoleStmt clone = (GrantRoleStmt) super.clone();
        if (granted_roles != null) {
            clone.granted_roles = granted_roles.clone();
        }
        if (grantee_roles != null) {
            clone.grantee_roles = grantee_roles.clone();
        }
        if (grantor != null) {
            clone.grantor = grantor;
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String separator = " ";
        if (is_grant) {
            result.append("grant");
            for (AccessPriv grantedRole : granted_roles) {
                result.append(separator).append(grantedRole);
                separator = ", ";
            }
            result.append(" to");
            separator = " ";
            for (RoleSpec grantee : grantee_roles) {
                result.append(separator).append(ParserUtil.identifierToSql(grantee.toString()));
                separator = ", ";
            }
            if (admin_opt) {
                result.append(" with admin option");
            }
        } else {
            result.append("revoke");
            if (admin_opt) {
                result.append(" admin option for");
            }
            for (AccessPriv grantedRole : granted_roles) {
                result.append(separator).append(grantedRole);
                separator = ", ";
            }
            result.append(" from");
            separator = " ";
            for (RoleSpec grantee : grantee_roles) {
                result.append(separator).append(ParserUtil.identifierToSql(grantee.toString()));
                separator = ", ";
            }
            result.append(behavior);
        }

        return result.toString();
    }
}
