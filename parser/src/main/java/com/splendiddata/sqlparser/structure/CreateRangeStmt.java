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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateRangeStmt extends Node {
    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "typeName")
    @XmlElement(name = "nameNode")
    public List<Value> typeName;

    /** range parameters (list of DefElem) */
    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    public List<DefElem> params;

    /**
     * Constructor
     */
    public CreateRangeStmt() {
        super(NodeTag.T_CreateRangeStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateRangeStmt to copy
     */
    public CreateRangeStmt(CreateRangeStmt original) {
        super(original);
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        if (original.params != null) {
            this.params = original.params.clone();
        }
    }

    @Override
    public CreateRangeStmt clone() {
        CreateRangeStmt clone = (CreateRangeStmt) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (params != null) {
            clone.params = params.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create type ").append(ParserUtil.nameToSql(typeName));

        if (params != null) {
            result.append(" (");
            String separator = "";
            for (DefElem param : params) {
                result.append(separator).append(param.defname).append(" = ").append(param.arg);
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
