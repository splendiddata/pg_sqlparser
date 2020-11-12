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

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Alter Object Rename Statement
 * <p>
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class RenameStmt extends Node {

    /** OBJECT_TABLE, OBJECT_COLUMN, etc */
    @XmlAttribute
    public ObjectType renameType;

    /** if column name, associated relation type */
    @XmlAttribute
    public ObjectType relationType;

    /** in case it's a table */
    @XmlElement
    public RangeVar relation;

    /**
     * in case it's some other object
     * <p>
     * In version 5.0 this has been altered from List&lt;Value&gt; to Node.
     * </p>
     */
    @XmlElement
    public Node object;

    /** argument types, if applicable
     * @deprecated since 5.0 - The function of objarg has moved to {@link #object} */
    @XmlElementWrapper(name = "objarg")
    @XmlElement
    @Deprecated
    public List<Node> objarg;

    /**
     * name of contained object (column, rule, trigger, etc)
     */
    @XmlAttribute
    public String subname;

    /** the new name */
    @XmlAttribute
    public String newname;

    /** RESTRICT or CASCADE behavior */
    @XmlAttribute
    public DropBehavior behavior;

    /** skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     *
     */
    public RenameStmt() {
        super(NodeTag.T_RenameStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the RenameStmt to copy
     */
    public RenameStmt(RenameStmt original) {
        super(original);
        this.renameType = original.renameType;
        this.relationType = original.relationType;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.object != null) {
            this.object = original.object.clone();
        }
        if (original.objarg != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".objargs is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.objarg);
        }
        this.subname = original.subname;
        this.newname = original.newname;
        this.behavior = original.behavior;
        this.missing_ok = original.missing_ok;
    }

    @Override
    public RenameStmt clone() {
        RenameStmt clone = (RenameStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (object != null) {
            clone.object = object.clone();
        }
        if (objarg != null) {
            clone.objarg = objarg.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter");

        if (relationType != null) {
            result.append(' ').append(relationType);
            if (missing_ok) {
                result.append(" if exists");
            }
            switch (renameType) {
            case OBJECT_CONSTRAINT:
                if (object == null) {
                    result.append(' ').append(relation);
                } else {
                    result.append(' ').append(ParserUtil.nameToSql(object));
                }

                result.append(" rename ").append(renameType).append(' ').append(ParserUtil.identifierToSql(subname))
                        .append(" to ").append(ParserUtil.identifierToSql(newname)).toString();

                return result.toString();
            case OBJECT_ATTRIBUTE:
            case OBJECT_COLUMN:
                result.append(' ').append(relation).append(" rename ").append(renameType).append(' ')
                        .append(ParserUtil.identifierToSql(subname)).append(" to ")
                        .append(ParserUtil.identifierToSql(newname)).toString();
                if (DropBehavior.DROP_CASCADE.equals(behavior)) {
                    result.append(" cascade");
                }
                return result.toString();
            default:
                result.append(' ').append(relation);
                break;
            }
        } else {
            if (ObjectType.OBJECT_TABCONSTRAINT.equals(renameType)) {
                result.append(" table");
            } else {
                result.append(' ').append(renameType);
            }
            if (missing_ok) {
                result.append(" if exists");
            }
            switch (renameType) {
            case OBJECT_OPFAMILY:
            case OBJECT_OPCLASS:
                @SuppressWarnings("unchecked") LinkedList<Value> nameList = new LinkedList<>((List<Value>) object);
                Value indexMethod = nameList.remove();
                result.append(' ').append(ParserUtil.nameListToSql(nameList)).append(" using ")
                        .append(ParserUtil.identifierToSql(indexMethod.val.str));
                break;
            case OBJECT_RULE:
            case OBJECT_TRIGGER:
                result.append(' ').append(ParserUtil.identifierToSql(subname)).append(" on ").append(relation);
                break;
            case OBJECT_FOREIGN_TABLE:
            case OBJECT_INDEX:
            case OBJECT_MATVIEW:
            case OBJECT_SEQUENCE:
            case OBJECT_TABLE:
            case OBJECT_VIEW:
                result.append(' ').append(relation);
                break;
            case OBJECT_TABCONSTRAINT:
                return result.append(' ').append(relation).append(" rename constraint ")
                        .append(ParserUtil.identifierToSql(subname)).append(" to ")
                        .append(ParserUtil.identifierToSql(newname)).toString();
            case OBJECT_ROLE:
            case OBJECT_DATABASE:
            case OBJECT_SCHEMA:
            case OBJECT_TABLESPACE:
                result.append(' ').append(ParserUtil.identifierToSql(subname));
                break;
            case OBJECT_DOMCONSTRAINT:
                return result.append("domain ").append(ParserUtil.nameToSql(object)).append(" rename constraint ")
                        .append(ParserUtil.identifierToSql(subname)).append(" to ")
                        .append(ParserUtil.identifierToSql(newname)).toString();
            case OBJECT_POLICY:
                result.append(' ').append(ParserUtil.identifierToSql(subname)).append(" on ").append(relation);
                break;
            default:
                result.append(' ').append(ParserUtil.nameToSql(object));
                break;
            }
        }

        result.append(" rename to ").append(ParserUtil.identifierToSql(newname));

        return result.toString();
    }
}
