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
 * create foreign data wrapper. Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement
public class CreateFdwStmt extends Node {

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
    public CreateFdwStmt() {
        super(NodeTag.T_CreateFdwStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateFdwStmt to copy
     */
    public CreateFdwStmt(CreateFdwStmt original) {
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
    public CreateFdwStmt clone() {
        CreateFdwStmt clone = (CreateFdwStmt) super.clone();
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

        result.append("create foreign data wrapper ").append(ParserUtil.identifierToSql(fdwname));

        if (func_options != null) {
            for (DefElem function : func_options) {
                if (function.arg == null) {
                    result.append(" no");
                }
                result.append(' ').append(function.defname);
                if (function.arg != null) {
                    result.append(' ').append(ParserUtil.nameToSql(function.arg));
                }
            }
        }

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
