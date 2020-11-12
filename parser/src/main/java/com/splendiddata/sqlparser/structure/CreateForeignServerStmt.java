/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
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
public class CreateForeignServerStmt extends Node {

    /** server name */
    @XmlAttribute
    public String servername;

    /** optional server type */
    @XmlAttribute
    public String servertype;

    /** optional server version */
    @XmlAttribute
    public String version;

    /** FDW name */
    @XmlAttribute
    public String fdwname;

    /** generic options to server */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * just do nothing if it already exists?
     * 
     * @since 5.0
     */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateForeignServerStmt() {
        super(NodeTag.T_CreateForeignServerStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateForeignServerStmt to copy
     */
    public CreateForeignServerStmt(CreateForeignServerStmt original) {
        super(original);
        this.servername = original.servername;
        this.servertype = original.servertype;
        this.version = original.version;
        this.fdwname = original.fdwname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.if_not_exists = original.if_not_exists;
    }

    @Override
    public CreateForeignServerStmt clone() {
        CreateForeignServerStmt clone = (CreateForeignServerStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create server ");
        if (if_not_exists) {
            result.append("if not exists ");
        }
        result.append(ParserUtil.identifierToSql(servername));

        if (servertype != null) {
            result.append(" type '").append(servertype).append('\'');
        }

        if (version != null) {
            result.append(" version '").append(version).append('\'');
        }

        if (fdwname != null) {
            result.append(" foreign data wrapper ").append(ParserUtil.identifierToSql(fdwname));
        }

        if (options != null) {
            result.append(" options (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
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
