/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.GrantTargetType;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 */
@XmlRootElement(namespace = "parser")
public class GrantStmt extends Node {

    /** true = GRANT, false = REVOKE */
    @XmlAttribute
    public boolean is_grant;

    /** type of the grant target */
    @XmlAttribute
    public GrantTargetType targtype;

    /** kind of object being operated on */
    @XmlAttribute
    public ObjectType objtype;

    /**
     * list of RangeVar nodes, FuncWithArgs nodes, or plain names (as Value strings)
     */
    @XmlElementWrapper(name = "objects")
    @XmlElement(name = "object")
    public List<Node> objects;

    /**
     * list of AccessPriv nodes<br>
     * privileges == NIL denotes ALL PRIVILEGES
     */
    @XmlElementWrapper(name = "privileges")
    @XmlElement(name = "privilege")
    public List<AccessPriv> privileges;

    /** list of PrivGrantee nodes */
    @XmlElementWrapper(name = "grantees")
    @XmlElement(name = "grantee")
    public List<RoleSpec> grantees;

    /** grant or revoke grant option */
    @XmlAttribute
    public boolean grant_option;

    /**
     * @since 14.0
     */
    @XmlElement
    public RoleSpec grantor;

    /** drop behavior (for REVOKE) */
    @XmlAttribute
    public DropBehavior behavior;

    /**
     * Constructor
     */
    public GrantStmt() {
        super(NodeTag.T_GrantStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The GrantStmt to copy
     */
    public GrantStmt(GrantStmt original) {
        super(original);
        this.is_grant = original.is_grant;
        this.targtype = original.targtype;
        this.objtype = original.objtype;
        if (original.objects != null) {
            this.objects = original.objects.clone();
        }
        if (original.privileges != null) {
            this.privileges = original.privileges.clone();
        }
        if (original.grantees != null) {
            this.grantees = original.grantees.clone();
        }
        this.grant_option = original.grant_option;
        if (original.grantor != null) {
            this.grantor = original.grantor.clone();
        }
        this.behavior = original.behavior;
    }

    @Override
    public GrantStmt clone() {
        GrantStmt clone = (GrantStmt) super.clone();
        if (objects != null) {
            clone.objects = objects.clone();
        }
        if (privileges != null) {
            clone.privileges = privileges.clone();
        }
        if (grantees != null) {
            clone.grantees = grantees.clone();
        }
        if (grantor != null) {
            clone.grantor = grantor.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (is_grant) {
            result.append("grant");
        } else {
            result.append("revoke");
            if (grant_option) {
                result.append(" grant option for");
            }
        }

        if (privileges == null) {
            result.append(" all privileges");
        } else {
            String separator = " ";
            for (AccessPriv privilege : privileges) {
                result.append(separator).append(privilege);
                separator = ", ";
            }
        }

        if (objtype != null) {
            result.append(" on ");
            if (GrantTargetType.ACL_TARGET_ALL_IN_SCHEMA.equals(targtype)) {
                result.append("all ");
            }
            result.append(switch (objtype) {
            case OBJECT_COLUMN -> "column";
            case OBJECT_DATABASE -> "database";
            case OBJECT_DOMAIN -> "domain";
            case OBJECT_FDW -> "foreign data wrapper";
            case OBJECT_FOREIGN_SERVER -> "foreign server";
            case OBJECT_FUNCTION -> "function";
            case OBJECT_LANGUAGE -> "language";
            case OBJECT_LARGEOBJECT -> "large object";
            case OBJECT_PROCEDURE -> "procedure";
            case OBJECT_ROUTINE -> "routine";
            case OBJECT_SCHEMA -> "schema";
            case OBJECT_TABLE -> "table";
            case OBJECT_SEQUENCE -> "sequence";
            case OBJECT_TABLESPACE -> "tablespace";
            case OBJECT_TYPE -> "type";
            case OBJECT_PROPGRAPH -> "property graph";
            default -> ParserUtil.reportUnknownValue("objtype", objtype, getClass());
            });

            result.append(switch (targtype) {
            case ACL_TARGET_ALL_IN_SCHEMA -> "s in schema";
            case ACL_TARGET_DEFAULTS -> "s";
            case ACL_TARGET_OBJECT -> "";
            default -> ParserUtil.reportUnknownValue("targtype", targtype, getClass());
            });
        }

        if (objects != null) {
            String separator = " ";
            for (Node object : objects) {
                result.append(separator);
                separator = ", ";
                if (object instanceof List) {
                    result.append(ParserUtil.nameToSql(object));
                } else {
                    result.append(object);
                }
            }
        }

        if (grantees != null) {
            String separator;
            if (is_grant) {
                separator = " to ";
            } else {
                separator = " from ";
            }
            for (RoleSpec grantee : grantees) {
                result.append(separator).append(grantee);
                separator = ", ";
            }
        }

        if (is_grant && grant_option) {
            result.append(" with grant option");
        }

        if (behavior != null) {
            result.append(switch (behavior) {
            case DROP_RESTRICT -> "";
            case DROP_CASCADE -> " cascade";
            default -> ParserUtil.reportUnknownValue("behavior", behavior, getClass());
            });
        }

        if (grantor != null) {
            result.append(" granted by ").append(grantor);
        }

        return result.toString();
    }
}
