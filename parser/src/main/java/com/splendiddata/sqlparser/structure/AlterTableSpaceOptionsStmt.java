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
public class AlterTableSpaceOptionsStmt extends Node {
    @XmlAttribute
    public String tablespacename;

    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    @XmlAttribute
    public boolean isReset;

    /**
     * Constructor
     */
    public AlterTableSpaceOptionsStmt() {
        super(NodeTag.T_AlterTableSpaceOptionsStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterTableSpaceOptionsStmt to copy
     */
    public AlterTableSpaceOptionsStmt(AlterTableSpaceOptionsStmt original) {
        super(original);
        this.tablespacename = original.tablespacename;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.isReset = original.isReset;
    }

    @Override
    public AlterTableSpaceOptionsStmt clone() {
        AlterTableSpaceOptionsStmt clone = (AlterTableSpaceOptionsStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter tablespace ").append(ParserUtil.identifierToSql(tablespacename));

        if (isReset) {
            result.append(" reset (");
        } else {
            result.append(" set (");
        }

        String separator = "";
        for (DefElem option : options) {
            result.append(separator).append(option.defname);
            separator = ", ";
            if (!isReset) {
                result.append(" = ").append(option.arg);
            }
        }

        return result.append(')').toString();
    }
}
