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
public class ExecuteStmt extends Node {

    /** The name of the plan to execute */
    @XmlAttribute
    public String name;

    /** Values to assign to parameters */
    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    public List<Node> params;

    /**
     * Constructor
     */
    public ExecuteStmt() {
        super(NodeTag.T_ExecuteStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            ExecuteStmt to copy
     */
    public ExecuteStmt(ExecuteStmt original) {
        super(original);
        this.name = original.name;
        if (original.params != null) {
            this.params = original.params.clone();
        }
    }

    @Override
    public ExecuteStmt clone() {
        ExecuteStmt clone = (ExecuteStmt) super.clone();
        if (params != null) {
            clone.params = params.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("execute ").append(ParserUtil.identifierToSql(name));

        if (params != null) {
            result.append(' ').append(params);
        }

        return result.toString();
    }
}
