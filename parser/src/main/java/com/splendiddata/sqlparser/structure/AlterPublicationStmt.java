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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.AlterPublicationAction;
import com.splendiddata.sqlparser.enums.DefElemAction;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class AlterPublicationStmt extends Node {
    /** Name of of the publication */
    @XmlAttribute
    public String pubname;

    /**
     * parameters used for ALTER PUBLICATION ... WITH * List of DefElem nodes
     */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /*
     * Parameters used for ALTER PUBLICATION ... ADD/DROP/SET publication objects.
     */
    /**
     * Optional list of publication objects
     * 
     * @since Postgres 15
     */
    @XmlElementWrapper(name = "pubobjects")
    @XmlElement(name = "pubobject")
    public List<PublicationObjSpec> pubobjects;

    /** Special publication for all tables in db */
    @XmlAttribute
    public boolean for_all_tables;

    /**
     * What action to perform with the given objects
     * 
     * @since Postgres 15
     */
    public AlterPublicationAction action;

    /**
     * parameters used for ALTER PUBLICATION ... ADD/DROP TABLE List of tables to add/drop
     * 
     * @deprecated since Postgres 15 - is replaced by @{link #pubobjects}
     */
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    @Deprecated(since = "Postgres 15", forRemoval = true)
    public List<Node> tables;

    /**
     * What action to perform with the tables
     * 
     * @deprecated since Postgres 15 = is replaced by @{link #action}
     */
    @XmlAttribute
    @Deprecated(since = "Postgres 15", forRemoval = true)
    public DefElemAction tableAction;

    /**
     * Constructor
     */
    public AlterPublicationStmt() {
        super(NodeTag.T_AlterPublicationStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterPublicationStmt to copy
     */
    public AlterPublicationStmt(AlterPublicationStmt original) {
        super(original);
        this.pubname = original.pubname;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.pubobjects != null) {
            this.pubobjects = original.pubobjects.clone();
        }
        this.for_all_tables = original.for_all_tables;
        this.action = original.action;
    }

    @Override
    public AlterPublicationStmt clone() {
        AlterPublicationStmt clone = (AlterPublicationStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        if (pubobjects != null) {
            clone.pubobjects = pubobjects.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("alter publication ")
                .append(ParserUtil.identifierToSql(pubname));
        if (action != null) {
            switch (action) {
            case AP_AddObjects:
                result.append(" add ");
                break;
            case AP_DropObjects:
                result.append(" drop ");
                break;
            case AP_SetObjects:
                result.append(" set ");
                break;
            default:
                return ParserUtil.reportUnknownValue("action: " + action.name(), this, getClass());
            }
            String separator = " ";
            if (pubobjects != null) {
                for (Node table : pubobjects) {
                    result.append(separator).append(table);
                    separator = ", ";
                }
            }
        }

        if (options != null && !options.isEmpty()) {
            String separator = " set (";
            for (DefElem opt : options) {
                result.append(separator).append(opt.defname);
                if (opt.arg != null) {
                    result.append(" ='").append(opt.arg).append('\'');
                }
                separator = ", ";
            }
            result.append(')');
        }
        return result.toString();
    }
}
