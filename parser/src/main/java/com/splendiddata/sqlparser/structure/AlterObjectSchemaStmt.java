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

import java.util.LinkedList;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Initially copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterObjectSchemaStmt extends Node {
    /** OBJECT_TABLE, OBJECT_TYPE, etc */
    @XmlAttribute
    public ObjectType objectType;

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

    /** the new schema */
    @XmlAttribute
    public String newschema;

    /** skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterObjectSchemaStmt() {
        super(NodeTag.T_AlterObjectSchemaStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the AlterObjectSchemaStmt to copy
     */
    public AlterObjectSchemaStmt(AlterObjectSchemaStmt original) {
        super(original);
        this.objectType = original.objectType;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.object != null) {
            this.object = original.object.clone();
        }
        this.newschema = original.newschema;
        this.missing_ok = original.missing_ok;
    }

    @Override
    public AlterObjectSchemaStmt clone() {
        AlterObjectSchemaStmt clone = (AlterObjectSchemaStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (object != null) {
            clone.object = object.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(objectType);

        if (missing_ok) {
            result.append(" if exists ");
        }

        switch (objectType) {
        case OBJECT_OPFAMILY:
        case OBJECT_OPCLASS:
            if (relation != null) {
                result.append(' ').append(relation);
                result.append(" using ").append(ParserUtil.nameToSql(object));
            } else if (object != null) {
                LinkedList<Value> objList = new LinkedList<>((List<Value>) object);
                Value using = objList.removeFirst();
                result.append(' ').append(ParserUtil.nameListToSql(objList)).append(" using ")
                        .append(ParserUtil.identifierToSql(using.val.str));
            }
            break;
        case OBJECT_OPERATOR:
            result.append(' ').append(ParserUtil.nameToSql(object));
            break;
        default:
            if (relation != null) {
                result.append(' ').append(relation);
            } else if (object != null) {
                result.append(' ').append(ParserUtil.nameToSql(object));
            }
            break;
        }

        result.append(" set schema ").append(ParserUtil.identifierToSql(newschema));

        return result.toString();
    }
}
