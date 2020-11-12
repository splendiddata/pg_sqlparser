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
public class AlterFdwStmt extends Node {
    /** foreign-data wrapper name */
    @XmlAttribute
    public String fdwname;

    /** HANDLER/VALIDATOR options */
    @XmlElementWrapper(name = "func_options")
    @XmlElement(name = "func_option")
    public List<DefElem> func_options;

    /** generic options to FDW */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public AlterFdwStmt() {
        super(NodeTag.T_AlterFdwStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterFdwStmt to copy
     */
    public AlterFdwStmt(AlterFdwStmt original) {
        super(original);
        this.fdwname = original.fdwname;
        if (original.func_options != null) {
            this.func_options = original.func_options.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public AlterFdwStmt clone() {
        AlterFdwStmt clone = (AlterFdwStmt) super.clone();
        if (func_options != null) {
            clone.func_options = func_options.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter foreign data wrapper ").append(ParserUtil.identifierToSql(fdwname));

        if (func_options != null) {
            for (DefElem option : func_options) {
                if (option.arg == null) {
                    result.append(" no ").append(option.defname);
                } else {
                    result.append(' ').append(option.defname).append(' ').append(ParserUtil.nameToSql(option.arg));
                }
            }
        }

        if (options != null) {
            result.append(" options (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defaction).append(ParserUtil.identifierToSql(option.defname));
                if (option.arg != null) {
                    result.append(" '").append(option.arg).append('\'');
                }
                separator = ", ";
            }
            result.append(')');
        }

        return result.toString();
    }
}
