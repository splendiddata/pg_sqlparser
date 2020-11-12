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

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterRoleStmt extends Node {
    /** role name */
    @XmlElement
    public Node role;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** +1 = add members, -1 = drop members */
    @XmlAttribute
    public int action;

    /**
     * Constructor
     */
    public AlterRoleStmt() {
        super(NodeTag.T_AlterRoleStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterRoleStmt to copy
     */
    public AlterRoleStmt(AlterRoleStmt original) {
        super(original);
        if (original.role != null) {
            this.role = original.role.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.action = original.action;
    }

    @Override
    public AlterRoleStmt clone() {
        AlterRoleStmt clone = (AlterRoleStmt) super.clone();
        if (role != null) {
            clone.role = role.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter role ");
        if (role == null) {
            result.append("none");
        } else {
            result.append(role);
        }

        if (options != null) {
            String separator = " with ";
            for (DefElem option : options) {
                switch (option.defname) {
                case "superuser":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nosuperuser");
                    } else {
                        result.append(separator).append("superuser");
                    }
                    break;
                case "createdb":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nocreatedb");
                    } else {
                        result.append(separator).append("createdb");
                    }
                    break;
                case "createrole":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nocreaterole");
                    } else {
                        result.append(separator).append("createrole");
                    }
                    break;
                case "inherit":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("noinherit");
                    } else {
                        result.append(separator).append("inherit");
                    }
                    break;
                case "canlogin":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nologin");
                    } else {
                        result.append(separator).append("login");
                    }
                    break;
                case "isreplication":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("noreplication");
                    } else {
                        result.append(separator).append("replication");
                    }
                    break;
                case "connectionlimit":
                    result.append(separator).append("connection limit ").append(option.arg);
                    break;
                case "password":
                    result.append(separator).append("password");
                    if (option.arg == null) {
                        result.append(" null");
                    } else {
                        result.append(' ').append(ParserUtil.toSqlTextString(option.arg));
                    }
                    break;
                case "encryptedPassword":
                    result.append(separator).append("encrypted password ")
                            .append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "unencryptedPassword":
                    result.append(separator).append("unencrypted password ")
                            .append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "validUntil":
                    result.append(separator).append("valid until ").append(ParserUtil.toSqlTextString(option.arg));
                    break;
                case "rolemembers":
                    /*
                     * Ok, now the role is not a role any more but suddenly a group
                     */
                    result.setLength(0);
                    result.append("alter group ").append(role);
                    if (action > 1) {
                        result.append(" add");
                    } else {
                        result.append(" drop");
                    }
                    separator = " user ";
                    if (option.arg instanceof List) {
                        for (RoleSpec member : (List<RoleSpec>) option.arg) {
                            result.append(separator).append(member);
                            separator = ", ";
                        }
                    }
                    break;
                case "bypassrls":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(separator).append("nobypassrls");
                    } else {
                        result.append(separator).append("bypassrls");
                    }
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option.defname", option.defname, getClass()));
                    break;
                }
                separator = " ";
            }
        }

        return result.toString();
    }
}
