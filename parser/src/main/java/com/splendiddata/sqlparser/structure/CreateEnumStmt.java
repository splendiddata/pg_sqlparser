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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateEnumStmt extends Node {

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "typeName")
    @XmlElement(name = "namenode")
    public List<Value> typeName;

    /** enum values (list of Value strings) */
    @XmlElementWrapper(name = "vals")
    @XmlElement(name = "val")
    public List<Value> vals;

    /**
     * Constructor
     */
    public CreateEnumStmt() {
        super(NodeTag.T_CreateEnumStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateEnumStmt to copy
     */
    public CreateEnumStmt(CreateEnumStmt original) {
        super(original);
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        if (original.vals != null) {
            this.vals = original.vals.clone();
        }
    }

    @Override
    public CreateEnumStmt clone() {
        CreateEnumStmt clone = (CreateEnumStmt) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (vals != null) {
            clone.vals = vals.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create type ").append(ParserUtil.nameToSql(typeName)).append(" as enum (");

        if (vals != null) {
            String separator = "";
            for (Value val : vals) {
                result.append(separator).append(ParserUtil.toSqlTextString(val));
                separator = ", ";
            }
        }
        result.append(')');

        return result.toString();
    }
}
