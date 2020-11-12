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
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class CreatePublicationStmt extends Node {
    /** Name of of the publication */
    @XmlAttribute
    public String pubname;

    /**
     * List of DefElem nodes
     */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Optional list of tables to add
     */
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    public List<RangeVar> tables;

    /** Special publication for all tables in db */
    @XmlAttribute
    public boolean for_all_tables;

    /**
     * Constructor
     */
    public CreatePublicationStmt() {
        super(NodeTag.T_CreatePublicationStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterPublicationStmt to copy
     */
    public CreatePublicationStmt(CreatePublicationStmt original) {
        super(original);
        this.pubname = original.pubname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.tables != null) {
            this.tables = original.tables.clone();
        }
        this.for_all_tables = original.for_all_tables;
    }

    @Override
    public CreatePublicationStmt clone() {
        CreatePublicationStmt clone = (CreatePublicationStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        if (tables != null) {
            clone.tables = tables.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("create publication ").append(ParserUtil.identifierToSql(pubname));
        if (for_all_tables) {
            result.append(" for all tables");
        } else if (tables != null && !tables.isEmpty()) {
            String separator = " for table ";
            for (RangeVar table : tables) {
                result.append(separator).append(table);
                separator = ", ";
            }
        }
        if (options != null && !options.isEmpty()) {
            String separator = " with (";
            for (DefElem opt : options) {
                result.append(separator).append(opt.defname);
                if (opt.arg != null) {
                    result.append(" = '").append(opt.arg).append('\'');
                }
                separator = ", ";
            }
            result.append(')');
        }
        return result.toString();
    }
}
