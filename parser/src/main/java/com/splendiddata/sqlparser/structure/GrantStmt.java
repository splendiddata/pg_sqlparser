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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.GrantTargetType;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
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
            switch (objtype) {
            case OBJECT_COLUMN:
                result.append("column");
                break;
            case OBJECT_DATABASE:
                result.append("database");
                break;
            case OBJECT_DOMAIN:
                result.append("domain");
                break;
            case OBJECT_FDW:
                result.append("foreign data wrapper");
                break;
            case OBJECT_FOREIGN_SERVER:
                result.append("foreign server");
                break;
            case OBJECT_FUNCTION:
                result.append("function");
                break;
            case OBJECT_LANGUAGE:
                result.append("language");
                break;
            case OBJECT_LARGEOBJECT:
                result.append("large object");
                break;
            case OBJECT_PROCEDURE:
                result.append("procedure");
                break;
            case OBJECT_ROUTINE:
                result.append("routine");
                break;
            case OBJECT_SCHEMA:
                result.append("schema");
                break;
            case OBJECT_TABLE:
                result.append("table");
                break;
            case OBJECT_SEQUENCE:
                result.append("sequence");
                break;
            case OBJECT_TABLESPACE:
                result.append("tablespace");
                break;
            case OBJECT_TYPE:
                result.append("type");
                break;
            default:
                result.append(" ??????? unknown GrantObjectType ").append(objtype).append(" in ")
                        .append(getClass().getName()).append(" ???????");
                break;
            }

            switch (targtype) {
            case ACL_TARGET_ALL_IN_SCHEMA:
                result.append("s in schema");
                break;
            case ACL_TARGET_DEFAULTS:
                result.append("s");
                break;
            case ACL_TARGET_OBJECT:
                break;
            default:
                result.append(" ??????? unknown GrantTargetType ").append(targtype).append(" in ")
                        .append(getClass().getName()).append(" ???????");
                break;
            }
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
            result.append(' ');
            switch (behavior) {
            case DROP_RESTRICT:
                //                result.append(" restrict");
                break;
            case DROP_CASCADE:
                result.append(" cascade");
                break;
            default:
                result.append(" ??????? unknonw DropBehavior").append(behavior).append(" in ")
                        .append(getClass().getName()).append(" ???????");
                break;
            }
        }

        return result.toString();
    }
}
