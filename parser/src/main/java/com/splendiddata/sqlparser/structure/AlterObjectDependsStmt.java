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
 * Initially copied from /postgresql-9.6.1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 4.0, Postgres version 9.6
 */
@XmlRootElement(namespace = "parser")
public class AlterObjectDependsStmt extends Node {
    /** OBJECT_FUNCTION, OBJECT_TRIGGER, etc. */
    @XmlAttribute
    public ObjectType objectType;

    /** in case a table is involved. */
    @XmlElement
    public RangeVar relation;

    /**
     * name of the object
     * 
     * @since 5.0
     */
    @XmlElement
    public Node object;

    /** Extension's name. */
    @XmlElement
    public Value extname;

    /**
     * set true to remove dep rather than add
     * 
     * @since 8.0 - Postgres version 13
     */
    public boolean remove;

    /**
     * Constructor
     */
    public AlterObjectDependsStmt() {
        super(NodeTag.T_AlterObjectDependsStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The alter statement to copy
     */
    public AlterObjectDependsStmt(AlterObjectDependsStmt original) {
        super(original);
        this.objectType = original.objectType;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        if (original.extname != null) {
            this.extname = original.extname.clone();
        }
        if (original.object != null) {
            this.object = original.object.clone();
        }
        this.remove = original.remove;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public AlterObjectDependsStmt clone() {
        AlterObjectDependsStmt clone = (AlterObjectDependsStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (object != null) {
            clone.object = object.clone();
        }
        if (extname != null) {
            clone.extname = extname.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter ").append(objectType);

        if (object != null) {
            result.append(' ').append(ParserUtil.nameToSql(object));
        }
        if (relation != null) {
            if (ObjectType.OBJECT_TRIGGER.equals(objectType)) {
                result.append(" on ").append(relation);
            } else {
                result.append(' ').append(relation);
            }
        }

        if (extname != null) {
            if (remove) {
                result.append(" no");
            }
            result.append(" depends on extension ").append(extname);
        }
        return result.toString();
    }
}
