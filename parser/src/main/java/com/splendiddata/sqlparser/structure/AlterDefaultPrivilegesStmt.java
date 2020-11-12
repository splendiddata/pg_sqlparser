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
public class AlterDefaultPrivilegesStmt extends Node {
    /** list of DefElem */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** GRANT/REVOKE action (with objects=NIL) */
    @XmlElement
    public GrantStmt action;

    /**
     * Constructor
     */
    public AlterDefaultPrivilegesStmt() {
        super(NodeTag.T_AlterDefaultPrivilegesStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterDefaultPrivilegesStmt to copy
     */
    public AlterDefaultPrivilegesStmt(AlterDefaultPrivilegesStmt original) {
        super(original);
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.action != null) {
            this.action = original.action.clone();
        }
    }

    @Override
    public AlterDefaultPrivilegesStmt clone() {
        AlterDefaultPrivilegesStmt clone = (AlterDefaultPrivilegesStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        if (action != null) {
            clone.action = action.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter default privileges");

        if (options != null) {
            for (DefElem option : options) {
                result.append(' ');
                switch (option.defname) {
                case "roles":
                    result.append(" for role");
                    break;
                case "schemas":
                    result.append(" in schema");
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option.defname", option.defname, getClass()));
                }

                if (option.arg instanceof List) {
                    String separator = " ";
                    for (Object node : (List<?>) option.arg) {
                        result.append(separator).append(ParserUtil.identifierToSql(node.toString()));
                        separator = ", ";
                    }
                }
            }
        }

        result.append(' ').append(action);

        return result.toString();
    }
}
