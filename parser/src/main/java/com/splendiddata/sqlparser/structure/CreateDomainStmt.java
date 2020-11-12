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
public class CreateDomainStmt extends Node {

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "domainname")
    @XmlElement(name = "nameNode")
    public List<Value> domainname;

    /** the base type */
    @XmlElement
    public TypeName typeName;

    /** untransformed COLLATE spec, if any */
    @XmlElement
    public CollateClause collClause;

    /** constraints (list of Constraint nodes) */
    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    public List<Node> constraints;

    /**
     * Constructor
     */
    public CreateDomainStmt() {
        super(NodeTag.T_CreateDomainStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateDomainStmt to copy
     */
    public CreateDomainStmt(CreateDomainStmt original) {
        super(original);
        if (original.domainname != null) {
            this.domainname = original.domainname.clone();
        }
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        if (original.collClause != null) {
            this.collClause = original.collClause.clone();
        }
        if (original.constraints != null) {
            this.constraints = original.constraints.clone();
        }
    }

    @Override
    public CreateDomainStmt clone() {
        CreateDomainStmt clone = (CreateDomainStmt) super.clone();
        if (domainname != null) {
            clone.domainname = domainname.clone();
        }
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (collClause != null) {
            clone.collClause = collClause.clone();
        }
        if (constraints != null) {
            clone.constraints = constraints.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create domain ").append(ParserUtil.nameToSql(domainname)).append(" as ").append(typeName);

        if (collClause != null) {
            result.append(collClause);
        }

        if (constraints != null) {
            for (Node constraint : constraints) {
                result.append(' ').append(constraint);
            }
        }

        return result.toString();
    }
}
