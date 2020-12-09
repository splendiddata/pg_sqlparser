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
public class AlterForeignServerStmt extends Node {
    /** server name */
    @XmlAttribute
    public String servername;

    /** optional server version */
    @XmlAttribute
    public String version;

    /** generic options to server */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** version specified */
    @XmlAttribute
    public boolean has_version;

    /**
     * Constructor
     */
    public AlterForeignServerStmt() {
        super(NodeTag.T_AlterForeignServerStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterForeignServerStmt to copy
     */
    public AlterForeignServerStmt(AlterForeignServerStmt original) {
        super(original);
        this.servername = original.servername;
        this.version = original.version;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.has_version = original.has_version;
    }

    @Override
    public AlterForeignServerStmt clone() {
        AlterForeignServerStmt clone = (AlterForeignServerStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter server ").append(ParserUtil.identifierToSql(servername));

        if (has_version) {
            if (version != null) {
                result.append(" version ").append(ParserUtil.toSqlTextString(version));
            }
        }

        if (options != null) {
            result.append(" options (");
            String separator = " ";
            for (DefElem option : options) {
                result.append(separator).append(option.defaction).append(ParserUtil.identifierToSql(option.defname));
                if (option.arg != null) {
                    result.append(' ').append(ParserUtil.toSqlTextString(option.arg));
                }
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
