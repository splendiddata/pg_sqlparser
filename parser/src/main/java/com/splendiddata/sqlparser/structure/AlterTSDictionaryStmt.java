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
public class AlterTSDictionaryStmt extends Node {
    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "dictname")
    @XmlElement(name = "nameNode")
    public List<Value> dictname;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public AlterTSDictionaryStmt() {
        super(NodeTag.T_AlterTSDictionaryStmt);
    }

    /**
     * Copy constructor
     *
     * @param orignial
     *            The AlterTSDictionaryStmt to copy
     */
    public AlterTSDictionaryStmt(AlterTSDictionaryStmt orignial) {
        super(orignial);
        if (orignial.dictname != null) {
            this.dictname = orignial.dictname.clone();
        }
        if (orignial.options != null) {
            this.options = orignial.options.clone();
        }
    }

    @Override
    public AlterTSDictionaryStmt clone() {
        AlterTSDictionaryStmt clone = (AlterTSDictionaryStmt) super.clone();
        if (dictname != null) {
            clone.dictname = dictname.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter text search dictionary ").append(ParserUtil.nameToSql(dictname));

        if (options != null) {
            result.append(" (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(ParserUtil.identifierToSql(option.defname));
                if (option.arg != null) {
                    result.append(" = ").append(option.arg);
                }
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
