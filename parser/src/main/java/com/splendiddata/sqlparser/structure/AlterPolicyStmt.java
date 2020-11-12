/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Alter POLICY Statement
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterPolicyStmt extends Node {
    /** Policy's name */
    @XmlAttribute
    public String policy_name;

    /** the table name the policy applies to */
    @XmlElement
    public RangeVar table;

    /** the roles associated with the policy */
    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public List<RoleSpec> roles;

    /** the policy's condition */
    @XmlElement
    public Node qual;

    /** the policy's WITH CHECK condition. */
    @XmlElement
    public Node with_check;

    /**
     * Constructor
     */
    public AlterPolicyStmt() {
        super(NodeTag.T_AlterPolicyStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The AlterPolicyStmt to copy
     */
    public AlterPolicyStmt(AlterPolicyStmt orig) {
        super(orig);
        this.policy_name = orig.policy_name;
        if (orig.table != null) {
            this.table = orig.table.clone();
        }
        if (orig.roles != null) {
            this.roles = orig.roles.clone();
        }
        if (orig.qual != null) {
            this.qual = orig.qual.clone();
        }
        if (orig.with_check != null) {
            this.with_check = orig.with_check.clone();
        }
    }

    @Override
    public AlterPolicyStmt clone() {
        AlterPolicyStmt clone = (AlterPolicyStmt) super.clone();
        if (table != null) {
            clone.table = table.clone();
        }
        if (roles != null) {
            clone.roles = roles.clone();
        }
        if (qual != null) {
            clone.qual = qual.clone();
        }
        if (with_check != null) {
            clone.with_check = with_check.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter policy ").append(policy_name).append(" on ").append(table);

        if (roles != null) {
            String separator = " to ";
            for (Node role : roles) {
                {
                    result.append(separator).append(role.toString());
                    separator = ", ";
                }
            }
        }

        if (qual != null) {
            result.append(" using(").append(qual).append(')');
        }

        if (with_check != null) {
            result.append(" with check(").append(with_check).append(')');
        }

        return result.toString();
    }
}
