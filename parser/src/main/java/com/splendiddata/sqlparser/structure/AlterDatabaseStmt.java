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
public class AlterDatabaseStmt extends Node {
    /** name of database to alter */
    @XmlAttribute
    public String dbname;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public AlterDatabaseStmt() {
        super(NodeTag.T_AlterDatabaseStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterDatabaseStmt to copy
     */
    public AlterDatabaseStmt(AlterDatabaseStmt original) {
        super(original);
        this.dbname = original.dbname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public AlterDatabaseStmt clone() {
        AlterDatabaseStmt clone = (AlterDatabaseStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter database ").append(ParserUtil.identifierToSql(dbname));

        if (options != null) {
            for (DefElem option : options) {
                switch (option.defname) {
                case "connectionlimit":
                    result.append(" with connection limit ").append(option.arg);
                    break;
                case "tablespace":
                    result.append(" set tablespace ").append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                default:
                    result.append(" set ").append(ParserUtil.identifierToSql(option.defname)).append(" to ")
                            .append(option.arg);
                }
            }
        }

        return result.toString();
    }
}
