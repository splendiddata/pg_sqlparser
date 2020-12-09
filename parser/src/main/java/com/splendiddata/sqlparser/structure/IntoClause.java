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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.OnCommitAction;

/**
 * Into clause in a select statement, copied from /postgresql-12beta2/src/include/nodes/primnodes.h
 * <p>
 * IntoClause - target information for SELECT INTO, CREATE TABLE AS, and CREATE MATERIALIZED VIEW
 * </p>
 * <p>
 * For CREATE MATERIALIZED VIEW, viewQuery is the parsed-but-not-rewritten SELECT Query for the view; otherwise it's
 * NULL. (Although it's actually Query*, we declare it as Node* to avoid a forward reference.)
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class IntoClause extends Node {

    /** target relation name */
    @XmlElement
    public RangeVar rel;

    /** column names to assign, or NIL */
    @XmlElementWrapper(name = "colnames")
    @XmlElement(name = "colname")
    public List<Value> colNames;

    /**
     * table access method
     * 
     * @since version 7.0 - Postgres 12
     */
    @XmlAttribute
    public String accessMethod;

    /** options from WITH clause */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** what do we do at COMMIT? */
    @XmlAttribute
    public OnCommitAction onCommit;

    /** table space to use, or NULL */
    @XmlElement
    public String tableSpaceName;

    /** materialized view's SELECT query */
    @XmlElement
    public Node viewQuery;

    /** true for WITH NO DATA */
    @XmlAttribute
    public boolean skipData;

    /**
     * Constructor
     */
    public IntoClause() {
        super(NodeTag.T_IntoClause);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The original
     */
    public IntoClause(IntoClause toCopy) {
        super(toCopy);
        if (toCopy.rel != null) {
            this.rel = toCopy.rel.clone();
        }
        if (toCopy.colNames != null) {
            this.colNames = toCopy.colNames.clone();
        }
        this.accessMethod = toCopy.accessMethod;
        if (toCopy.options != null) {
            this.options = toCopy.options.clone();
        }
        this.tableSpaceName = toCopy.tableSpaceName;
        if (toCopy.viewQuery != null) {
            this.viewQuery = toCopy.viewQuery.clone();
        }
        this.skipData = toCopy.skipData;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (rel != null) {
            result.append(rel);
        }

        if (colNames != null) {
            result.append(" (");
            String separator = "";
            for (Value colName : colNames) {
                result.append(separator).append(ParserUtil.identifierToSql(colName.toString()));
                separator = ", ";
            }
            result.append(')');
        }

        if (options != null) {
            if (options.size() == 1 && "oids".equals(options.get(0).defname.toLowerCase())) {
                Value val = (Value) options.get(0).arg;
                if (val == null || val.val.ival == 0) {
                    result.append(" without oids");
                } else {
                    result.append(" with oids");
                }
            } else {
                result.append(" with (");
                String separator = "";
                for (DefElem option : options) {
                    result.append(separator).append(option.defname.toLowerCase());
                    if (option.arg != null) {
                        result.append(" = ").append(option.arg);
                    }
                    separator = ", ";
                }
                result.append(')');
            }
        }

        if (onCommit != null) {
            switch (onCommit) {
            case ONCOMMIT_DELETE_ROWS:
                result.append(" on commit delete rows");
                break;
            case ONCOMMIT_DROP:
                result.append(" on commit drop");
                break;
            case ONCOMMIT_NOOP:
                break;
            case ONCOMMIT_PRESERVE_ROWS:
                result.append(" on commit reserve rows");
                break;
            default:
                throw new AssertionError("Unknown OnCommitAction: " + onCommit);
            }
        }

        if (tableSpaceName != null) {
            result.append(" tablespace ").append(ParserUtil.identifierToSql(tableSpaceName));
        }

        if (viewQuery != null) {
            result.append(" viewQuery=").append(viewQuery);
        }
        
        return result.toString();
    }

    @Override
    public IntoClause clone() {
        IntoClause clone = (IntoClause) super.clone();
        if (rel != null) {
            clone.rel = rel.clone();
        }
        if (colNames != null) {
            clone.colNames = colNames.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        if (viewQuery != null) {
            clone.viewQuery = viewQuery.clone();
        }
        return clone;
    }
}
