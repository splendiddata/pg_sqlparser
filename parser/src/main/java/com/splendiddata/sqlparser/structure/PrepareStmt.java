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
public class PrepareStmt extends Node {

    /** Name of plan, arbitrary */
    @XmlAttribute
    public String name;

    /** Types of parameters (List of TypeName) */
    @XmlElementWrapper(name = "argtypes")
    @XmlElement(name = "argtype")
    public List<TypeName> argtypes;

    /** The query itself (as a raw parsetree) */
    @XmlElement
    public Node query;

    /**
     * Constructor
     */
    public PrepareStmt() {
        super(NodeTag.T_PrepareStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The PrepareStmt to copy
     */
    public PrepareStmt(PrepareStmt original) {
        super(original);
        this.name = original.name;
        if (original.argtypes != null) {
            this.argtypes = original.argtypes.clone();
        }
        if (original.query != null) {
            this.query = original.query.clone();
        }
    }

    @Override
    public PrepareStmt clone() {
        PrepareStmt clone = (PrepareStmt) super.clone();
        if (argtypes != null) {
            clone.argtypes = argtypes.clone();
        }
        if (query != null) {
            clone.query = query.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("prepare ").append(ParserUtil.identifierToSql(name));
        if (argtypes != null) {
            result.append(' ').append(argtypes);
        }

        result.append(" as ").append(query);

        return result.toString();
    }
}
