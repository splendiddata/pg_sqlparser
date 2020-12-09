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

import jakarta.xml.bind.annotation.XmlAttribute;
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
public class CreateForeignTableStmt extends Node {
    @XmlElement
    public CreateStmt base = new CreateStmt();

    @XmlAttribute
    public String servername;

    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public CreateForeignTableStmt() {
        super(NodeTag.T_CreateForeignTableStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateForeignTableStmt to copy
     */
    public CreateForeignTableStmt(CreateForeignTableStmt original) {
        super(original);
        if (original.base != null) {
            this.base = original.base.clone();
        }
        this.servername = original.servername;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public CreateForeignTableStmt clone() {
        CreateForeignTableStmt clone = (CreateForeignTableStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create foreign").append(base.toStringStartingWithTable()).append(" server ")
                .append(ParserUtil.identifierToSql(servername));

        if (options != null) {
            result.append(" options (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(ParserUtil.identifierToSql(option.defname)).append(' ')
                        .append(ParserUtil.toSqlTextString(option.arg));
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
