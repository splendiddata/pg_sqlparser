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
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterUserMappingStmt extends Node {
    /** user role */
    @XmlElement
    public Node user;

    /** server name */
    @XmlAttribute
    public String servername;

    /** generic options to server */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public AlterUserMappingStmt() {
        super(NodeTag.T_AlterUserMappingStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterUserMappingStmt to copy
     */
    public AlterUserMappingStmt(AlterUserMappingStmt original) {
        super(original);
        if (original.user != null) {
            this.user = original.user.clone();
        }
        this.servername = original.servername;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public AlterUserMappingStmt clone() {
        AlterUserMappingStmt clone = (AlterUserMappingStmt) super.clone();
        if (user != null) {
            clone.user = user.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter user mapping for ");
        if (user == null) {
            result.append("public");
        } else {
            result.append(user);
        }

        result.append(" server ").append(ParserUtil.identifierToSql(servername));

        if (options != null) {
            result.append(" options(");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defaction).append(option.defname);
                separator = ", ";
                if (option.arg != null) {
                    result.append(' ').append(ParserUtil.toSqlTextString(option.arg));
                }
            }
            result.append(')');
        }

        return result.toString();
    }
}
