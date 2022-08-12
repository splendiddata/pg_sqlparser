/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CommentStmt extends Node {

    /** Object's type */
    @XmlAttribute
    public ObjectType objtype;

    /**
     * Qualified name of the object
     * 
     * @since 5.0
     */
    @XmlElement
    public Node object;

    /**
     * Constructor
     */
    public CommentStmt() {
        super(NodeTag.T_CommentStmt);
    }

    /**
     * Constructor
     *
     * @param original
     *            The CommentStmt to copy
     */
    public CommentStmt(CommentStmt original) {
        super(original);
        this.objtype = original.objtype;
        if (original.object != null) {
            this.object = original.object.clone();
        }
        this.comment = original.comment;
    }

    /** Comment to insert, or NULL to remove */
    @XmlElement
    public String comment;

    @Override
    public CommentStmt clone() {
        CommentStmt clone = (CommentStmt) super.clone();
        if (object != null) {
            clone.object = object.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("comment on ").append(objtype);
        switch (objtype) {
        case OBJECT_RULE:
        case OBJECT_TABCONSTRAINT:
        case OBJECT_TRIGGER:
            result.append(' ').append(ParserUtil.elementOnObjectNameToSql((List<Node>) object, "on"));
            break;
        case OBJECT_DOMCONSTRAINT:
            result.append("constraint ").append(ParserUtil.elementOnObjectNameToSql((List<Node>) object, "on domain"));
            break;
        case OBJECT_OPFAMILY:
        case OBJECT_OPCLASS:
            List<Value> nameValueList = (List<Value>) object;
            result.append(' ').append(ParserUtil.nameListToSql(nameValueList.subList(1, nameValueList.size())))
                    .append(" using ").append(ParserUtil.nameToSql(nameValueList.get(0)));
            break;
        case OBJECT_CAST:
            List<TypeName> typeNameList = (List<TypeName>) object;
            result.append(" (").append(typeNameList.get(0).toString()).append(" as ")
                    .append(typeNameList.get(1).toString()).append(')');
            break;
        case OBJECT_LARGEOBJECT:
            result.append(' ').append(object);
            break;
        case OBJECT_AGGREGATE:
            result.append(' ').append(ParserUtil.nameToSql(object));
            if (((ObjectWithArgs) object).objargs == null || ((ObjectWithArgs) object).objargs.isEmpty()) {
                result.append("(*)");
            }
            break;
        case OBJECT_ACCESS_METHOD:
        case OBJECT_ATTRIBUTE:
        case OBJECT_COLLATION:
        case OBJECT_COLUMN:
        case OBJECT_CONSTRAINT:
        case OBJECT_CONVERSION:
        case OBJECT_DATABASE:
        case OBJECT_DOMAIN:
        case OBJECT_EVENT_TRIGGER:
        case OBJECT_EXTENSION:
        case OBJECT_FDW:
        case OBJECT_FOREIGN_SERVER:
        case OBJECT_FOREIGN_TABLE:
        case OBJECT_FUNCTION:
        case OBJECT_INDEX:
        case OBJECT_LANGUAGE:
        case OBJECT_MATVIEW:
        case OBJECT_OPERATOR:
        case OBJECT_POLICY:
        case OBJECT_PUBLICATION:
        case OBJECT_ROLE:
        case OBJECT_SCHEMA:
        case OBJECT_SEQUENCE:
        case OBJECT_STATISTIC_EXT:
        case OBJECT_SUBSCRIPTION:
        case OBJECT_TABLE:
        case OBJECT_TABLESPACE:
        case OBJECT_TRANSFORM:
        case OBJECT_TSCONFIGURATION:
        case OBJECT_TSDICTIONARY:
        case OBJECT_TSPARSER:
        case OBJECT_TSTEMPLATE:
        case OBJECT_TYPE:
        case OBJECT_VIEW:
        default:
            result.append(' ').append(ParserUtil.nameToSql(object));
            break;
        }
        result.append(" is ").append(ParserUtil.toSqlTextString(comment));

        return result.toString();
    }
}
