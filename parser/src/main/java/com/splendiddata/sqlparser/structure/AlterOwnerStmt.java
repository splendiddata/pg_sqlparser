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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.ObjectType;

/**
 * Alter Object Owner Statement
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterOwnerStmt extends Node {
    /** OBJECT_TABLE, OBJECT_TYPE, etc */
    @XmlAttribute
    public ObjectType objectType;

    /** in case it's a table */
    @XmlElement
    public RangeVar relation;

    /**
     * in case it's some other object
     * <p>
     * Since 5.0 this is a node instead of a List&lt;Value&gt;
     * </p>
     */
    @XmlElement
    public Node object;

    /**
     * argument types, if applicable
     * 
     * @deprecated since 5.0 - it's function is taken by {@link #object}
     */
    @XmlElementWrapper(name = "objarg")
    @XmlElement(name = "arg")
    @Deprecated
    public List<Node> objarg;

    /** the new owner */
    @XmlElement
    public Node newowner;

    /**
     * Constructor
     */
    public AlterOwnerStmt() {
        super(NodeTag.T_AlterOwnerStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterOwnerStmt to copy
     */
    public AlterOwnerStmt(AlterOwnerStmt original) {
        super(original);
        this.objectType = original.objectType;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.object != null) {
            this.object = original.object.clone();
        }
        if (original.objarg != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".objarg is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + original.objarg);
        }
        if (original.newowner != null) {
            this.newowner = original.newowner.clone();
        }
    }

    @Override
    public AlterOwnerStmt clone() {
        AlterOwnerStmt clone = (AlterOwnerStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (object != null) {
            clone.object = object.clone();
        }
        if (objarg != null) {
            clone.objarg = objarg.clone();
        }
        if (newowner != null) {
            clone.newowner = newowner.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(objectType);

        if (relation != null) {
            result.append(' ').append(relation);
        } else if (object != null) {
            switch (objectType) {
            case OBJECT_LARGEOBJECT:
                result.append(' ').append(object);
                break;
            case OBJECT_OPERATOR:
                result.append(' ').append(object);
                break;
            case OBJECT_OPFAMILY:
            case OBJECT_OPCLASS:
                LinkedList<Value> objList = new LinkedList<>((List<Value>) object);
                Value using = objList.removeFirst();
                result.append(' ').append(ParserUtil.nameListToSql(objList)).append(" using ")
                        .append(ParserUtil.identifierToSql(using.val.str));
                break;
            default:
                result.append(' ').append(ParserUtil.nameToSql(object));
                break;
            }
        }

        result.append(" owner to ").append(newowner);

        return result.toString();
    }
}
