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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * CREATE TRANSFORM Statement
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateTransformStmt extends Node {
    @XmlAttribute
    public boolean replace;

    @XmlElement
    public TypeName type_name;

    @XmlAttribute
    public String lang;

    @XmlElement
    public ObjectWithArgs fromsql;

    @XmlElement
    public ObjectWithArgs tosql;

    /**
     * Constructor
     */
    public CreateTransformStmt() {
        super(NodeTag.T_CreateTransformStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateTransformStmt to copy
     */
    public CreateTransformStmt(CreateTransformStmt original) {
        super(original);
        this.replace = original.replace;
        if (original.type_name != null) {
            this.type_name = original.type_name.clone();
        }
        this.lang = original.lang;
        if (original.fromsql != null) {
            this.fromsql = original.fromsql.clone();
        }
        if (original.tosql != null) {
            this.tosql = original.tosql.clone();
        }
    }

    @Override
    public CreateTransformStmt clone() {
        CreateTransformStmt clone = (CreateTransformStmt) super.clone();
        if (type_name != null) {
            clone.type_name = type_name;
        }
        if (fromsql != null) {
            clone.fromsql = fromsql.clone();
        }
        if (tosql != null) {
            clone.tosql = tosql.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (replace) {
            result.append("or replace ");
        }

        result.append("transform ").append("for ").append(type_name).append(" language ").append(lang).append(" (");
        if (fromsql != null) {
            result.append("from sql with function ").append(fromsql);
        }

        if (tosql != null) {
            if (fromsql != null) {
                result.append(", ");
            }
            result.append("to sql with function ").append(tosql);
        }
        result.append(")");

        return result.toString();
    }
}
