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
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * <p>
 * ----------------------<br>
 * Copy Statement
 * </p>
 * <p>
 * We support "COPY relation FROM file", "COPY relation TO file", and "COPY (query) TO file". In any given CopyStmt,
 * exactly one of "relation" and "query" must be non-NULL.<br>
 * ----------------------
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CopyStmt extends Node {

    /** the relation to copy */
    @XmlElement
    public RangeVar relation;

    /** the SELECT query to copy */
    @XmlElement
    public Node query;

    /**
     * List of column names (as Strings), or NIL for all columns
     */
    @XmlElementWrapper(name = "attlist")
    @XmlElement(name = "attribute")
    public List<Node> attlist;

    /** TO or FROM */
    @XmlAttribute
    public boolean is_from;

    /** is 'filename' a program to popen? */
    @XmlAttribute
    public boolean is_program;

    /** filename, or NULL for STDIN/STDOUT */
    @XmlAttribute
    public String filename;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * WHERE condition (or NULL)
     * 
     * @since version 7.0 - Postgres 12
     */
    @XmlElement
    public Node whereClause;

    /**
     * Constructor
     */
    public CopyStmt() {
        super(NodeTag.T_CopyStmt);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The CopyStmt to copy
     */
    public CopyStmt(CopyStmt toCopy) {
        super(toCopy);
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        if (toCopy.query != null) {
            this.query = toCopy.query.clone();
        }
        if (toCopy.attlist != null) {
            this.attlist = toCopy.attlist.clone();
        }
        this.is_from = toCopy.is_from;
        this.is_program = toCopy.is_program;
        this.filename = toCopy.filename;
        if (toCopy.options != null) {
            this.options = toCopy.options.clone();
        }
        if (toCopy.whereClause != null) {
            this.whereClause = toCopy.whereClause.clone();
        }
    }

    @Override
    public CopyStmt clone() {
        CopyStmt clone = (CopyStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (query != null) {
            clone.query = query.clone();
        }
        if (attlist != null) {
            clone.attlist = attlist.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("copy ");

        if (relation != null) {
            result.append(relation);
            if (attlist != null) {
                result.append('(');
                String separator = "";
                for (Node att : attlist) {
                    result.append(separator).append(ParserUtil.identifierToSql(att.toString()));
                    separator = ", ";
                }
                result.append(')');
            }
        } else if (query != null) {
            result.append('(').append(query).append(')');
        }

        if (is_from) {
            result.append(" from");
            if (filename == null) {
                result.append(" stdin");
            }
        } else {
            result.append(" to");
            if (filename == null) {
                result.append(" stdout");
            }
        }

        if (is_program) {
            result.append(" program");
        }

        if (filename != null) {
            result.append(' ').append(ParserUtil.toSqlTextString(filename));
        }

        if (options != null) {
            result.append(" with(");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                separator = ", ";
                switch (option.defname) {
                case "format":
                    result.append(' ').append(ParserUtil.identifierToSql(option.arg.toString()));
                    break;
                case "iod":
                    result.append("s ").append(option.arg);
                    break;
                case "oids":
                    if (option.arg != null) {
                        result.append(" ").append(option.arg);
                    }
                    break;
                case "freeze":
                case "header":
                case "force_quote":
                case "force_not_null":
                case "force_null":
                    if (option.arg != null) {
                        result.append(' ').append(option.arg);
                    }
                    break;
                case "delimiter":
                case "null":
                case "quote":
                case "esc":
                case "encoding":
                case "escape":
                    result.append(' ').append(ParserUtil.toSqlTextString(option.arg.toString()));
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("option.defname", option.defname, getClass()));
                    break;
                }
            }
            result.append(')');
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        return result.toString();
    }
}
