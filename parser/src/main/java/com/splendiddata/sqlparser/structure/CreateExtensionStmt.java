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
public class CreateExtensionStmt extends Node {

    @XmlAttribute
    public String extname;

    /** just do nothing if it already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public CreateExtensionStmt() {
        super(NodeTag.T_CreateExtensionStmt);
    }

    /**
     * Constructor
     *
     * @param original
     *            The CreateExtensionStmt to copy
     */
    public CreateExtensionStmt(CreateExtensionStmt original) {
        super(original);
        this.extname = original.extname;
        this.if_not_exists = original.if_not_exists;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public CreateExtensionStmt clone() {
        CreateExtensionStmt clone = (CreateExtensionStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create extension ");

        if (if_not_exists) {
            result.append("if not exists ");
        }

        result.append(ParserUtil.identifierToSql(extname));

        if (options != null) {
            result.append(" with");
            for (DefElem option : options) {
                switch (option.defname) {
                case "new_version":
                    result.append(" version ");
                    result.append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                case "old_version":
                    result.append(" from ");
                    result.append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                case "cascade":
                    result.append(' ').append(option.defname);
                    break;
                default:
                    result.append(' ').append(option.defname).append(' ');
                    result.append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                }
            }
        }
        return result.toString();
    }
}
