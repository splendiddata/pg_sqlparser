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
public class AlterExtensionStmt extends Node {
    @XmlAttribute
    public String extname;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public AlterExtensionStmt() {
        super(NodeTag.T_AlterExtensionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterExtensionStmt to copy
     */
    public AlterExtensionStmt(AlterExtensionStmt original) {
        super(original);
        this.extname = original.extname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public AlterExtensionStmt clone() {
        AlterExtensionStmt clone = (AlterExtensionStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter extension ").append(ParserUtil.identifierToSql(extname)).append(" update");

        if (options != null) {
            for (DefElem option : options) {
                result.append(' ');
                switch (option.defname) {
                case "new_version":
                    result.append("to ").append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option", option.defname, getClass()));
                }
            }
        }

        return result.toString();
    }
}
