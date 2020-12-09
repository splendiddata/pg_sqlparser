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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.RoleSpecType;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class RoleSpec extends Node {
    /** Type of this rolespec */
    @XmlAttribute
    public RoleSpecType roletype;

    /** filled only for ROLESPEC_CSTRING */
    @XmlAttribute
    public String rolename;

    /**
     * Constructor
     */
    public RoleSpec() {
        super(NodeTag.T_RoleSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public RoleSpec(RoleSpec original) {
        super(original);
        this.roletype = original.roletype;
        this.rolename = original.rolename;
    }

    @Override
    public RoleSpec clone() {
        return (RoleSpec) super.clone();
    }

    @Override
    public String toString() {
        switch (roletype) {
        case ROLESPEC_CSTRING:
            return ParserUtil.identifierToSql(rolename);
        case ROLESPEC_CURRENT_USER:
        case ROLESPEC_PUBLIC:
        case ROLESPEC_SESSION_USER:
            return roletype.toString();
        default:
            throw new AssertionError("RoleSpecType " + roletype
                    + " not implemented in com.splendiddata.sqlparser.structure.RoleSpec.toString()");
        }
    }
}
