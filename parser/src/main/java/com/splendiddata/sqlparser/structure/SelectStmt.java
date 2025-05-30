/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.LimitOption;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.SetOperation;

/**
 * Select statement as defined in /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class SelectStmt extends Expr {

    /*
     * These fields are used only in "leaf" SelectStmts.
     */
    /** NULL, list of DISTINCT ON exprs, or lcons(NIL,NIL) for all (SELECT DISTINCT) */
    @XmlElementWrapper(name = "distinctClause")
    @XmlElement(name = "distinct")
    public List<Node> distinctClause;

    /** target for SELECT INTO */
    @XmlElement
    public IntoClause intoClause;

    /** the target list (of ResTarget) */
    @XmlElementWrapper(name = "targetList")
    @XmlElement(name = "target")
    public List<ResTarget> targetList;

    /** the FROM clause */
    @XmlElementWrapper(name = "fromClause")
    @XmlElement(name = "from")
    public List<Node> fromClause;

    /** WHERE qualification */
    @XmlElement
    public Node whereClause;

    /** GROUP BY clauses */
    @XmlElementWrapper(name = "groupClause")
    @XmlElement(name = "groupBy")
    public List<Node> groupClause;

    /**
     * Is this GROUP BY DISTINCT?
     * 
     * @since 14.0
     */
    public boolean groupDistinct;

    /** HAVING conditional-expression */
    @XmlElement
    public Node havingClause;

    /** WINDOW window_name AS (...), ... */
    @XmlElementWrapper(name = "windowClause")
    @XmlElement(name = "window")
    public List<WindowDef> windowClause;

    /*
     * In a "leaf" node representing a VALUES list, the above fields are all null, and instead this field is set. Note
     * that the elements of the sublists are just expressions, without ResTarget decoration. Also note that a list
     * element can be DEFAULT (represented as a SetToDefault node), regardless of the context of the VALUES list. It's
     * up to parse analysis to reject that where not valid.
     */
    /** untransformed list of expression lists */
    @XmlElementWrapper(name = "valuesLists")
    @XmlElement(name = "values")
    public List<List<Node>> valuesLists;

    /*
     * These fields are used in both "leaf" SelectStmts and upper-level SelectStmts.
     */
    /** sort clause (a list of SortBy's) */
    @XmlElementWrapper(name = "sortClause")
    @XmlElement(name = "sortBy")
    public List<SortBy> sortClause;

    /** # of result tuples to skip */
    @XmlElement
    public Node limitOffset;

    /** # of result tuples to return */
    @XmlElement
    public Node limitCount;

    /**
     * limit type
     * 
     * @since 8.0 - Postgres version 13
     */
    @XmlAttribute
    public LimitOption limitOption;

    /** FOR UPDATE (list of LockingClause's) */
    @XmlElementWrapper(name = "lockingClause")
    @XmlElement(name = "lock")
    public List<LockingClause> lockingClause;

    /** WITH clause */
    @XmlElement
    public WithClause withClause;

    /*
     * These fields are used only in upper-level SelectStmts.
     */
    /** type of set op */
    @XmlElement
    public SetOperation op;

    /** ALL specified? */
    @XmlAttribute
    public boolean all;

    /** left child */
    @XmlElement
    public SelectStmt larg;

    /** right child */
    @XmlElement
    public SelectStmt rarg;

    /**
     * The offset of this Stmt relative to the start of the sql stream
     * <p>
     * 
     * @since Postgres 18
     */
    public Location stmt_location;

    /**
     * length in bytes; 0 means "rest of string"
     * 
     * @since Postgres 18
     */
    @XmlTransient
    public long stmt_len;

    /**
     * Constructor
     */
    public SelectStmt() {
        super(NodeTag.T_SelectStmt);
    }

    /**
     * Copy constructor
     *
     * @param other
     *            The SelectStmt to copy
     */
    public SelectStmt(SelectStmt other) {
        super(other);
        if (other.distinctClause != null) {
            this.distinctClause = other.distinctClause.clone();
        }
        if (other.intoClause != null) {
            this.intoClause = other.intoClause.clone();
        }
        if (other.targetList != null) {
            this.targetList = other.targetList.clone();
        }
        if (other.fromClause != null) {
            this.fromClause = other.fromClause.clone();
        }
        if (other.whereClause != null) {
            this.whereClause = other.whereClause.clone();
        }
        if (other.groupClause != null) {
            this.groupClause = other.groupClause.clone();
        }
        if (other.havingClause != null) {
            this.havingClause = other.havingClause.clone();
        }
        if (other.windowClause != null) {
            this.windowClause = other.windowClause.clone();
        }
        if (other.valuesLists != null) {
            this.valuesLists = other.valuesLists.clone();
        }
        if (other.sortClause != null) {
            this.sortClause = other.sortClause.clone();
        }
        if (other.limitOffset != null) {
            this.limitOffset = other.limitOffset.clone();
        }
        if (other.limitCount != null) {
            this.limitCount = other.limitCount.clone();
        }
        this.limitOption = other.limitOption;
        if (other.lockingClause != null) {
            this.lockingClause = other.lockingClause.clone();
        }
        if (other.withClause != null) {
            this.withClause = other.withClause.clone();
        }
        this.op = other.op;
        this.all = other.all;
        if (other.larg != null) {
            this.larg = other.larg.clone();
        }
        if (other.rarg != null) {
            this.rarg = other.rarg.clone();
        }
        if (other.stmt_location != null) {
            this.stmt_location = other.stmt_location.clone();
        }
        this.stmt_len = other.stmt_len;
        this.groupDistinct = other.groupDistinct;
    }

    @Override
    public String toString() {
        String leadingSpace = "";
        StringBuilder result = new StringBuilder();

        if (withClause != null) {
            result.append(withClause);
            leadingSpace = " ";
        }

        if (op == null) {
            if (valuesLists == null) {
                result.append(leadingSpace).append("select");
                leadingSpace = " ";
            } else {
                result.append("values ");
                String separator = "";
                for (Node value : valuesLists) {
                    result.append(separator);
                    if (NodeTag.T_List.equals(value.type)) {
                        /*
                         * Lists print brackets themselves
                         */
                        result.append(value);
                    } else {
                        result.append('(').append(value).append(')');
                    }
                    separator = ", ";
                }
            }
        } else {
            if (larg != null) {
                result.append(leadingSpace);
                if (larg.sortClause == null && larg.limitOffset == null && larg.withClause == null) {
                    result.append(larg);
                } else {
                    result.append('(').append(larg).append(')');
                }
                leadingSpace = " ";
            }

            switch (op) {
            case SETOP_EXCEPT:
                result.append(leadingSpace).append("except ");
                break;
            case SETOP_INTERSECT:
                result.append(leadingSpace).append("intersect ");
                break;
            case SETOP_NONE:
                result.append(leadingSpace).append("?????? NONE ??????? ");
                break;
            case SETOP_UNION:
                result.append(leadingSpace).append("union ");
                break;
            default:
                throw new AssertionError("Unknown SetOperation: " + op);
            }
            leadingSpace = " ";

            if (all) {
                result.append("all ");
            }

            if (rarg != null) {
                if (rarg.sortClause == null && rarg.limitOffset == null && rarg.withClause == null) {
                    result.append(rarg);
                } else {
                    result.append('(').append(rarg).append(')');
                }
            }
        }

        if (distinctClause != null) {
            if (distinctClause.isEmpty()) {
                result.append(" distinct");
            } else {
                String separator = " distinct on (";
                for (Node distinct : distinctClause) {
                    result.append(separator).append(distinct);
                    separator = ", ";
                }
                result.append(')');
            }
        }

        if (targetList != null) {
            String separator = " ";
            for (Object target : targetList) {
                result.append(separator).append(target);
                separator = ", ";
            }
        }

        if (intoClause != null) {
            result.append(" into ");

            if (intoClause.rel != null) {
                switch (intoClause.rel.relpersistence) {
                case RELPERSISTENCE_PERMANENT:
                    break;
                case RELPERSISTENCE_TEMP:
                    result.append("temporary ");
                    break;
                case RELPERSISTENCE_UNLOGGED:
                    result.append("unlogged ");
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("intoClause.relpersistence",
                            intoClause.rel.relpersistence, getClass()));
                }
            }

            result.append(intoClause);
        }

        if (fromClause != null) {
            String separator = " from ";
            for (Object target : fromClause) {
                result.append(separator).append(target);
                separator = ", ";
            }
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        if (groupClause != null) {
            String separator = " group by ";
            if (groupDistinct) {
                separator += "distinct ";
            }
            for (Object target : groupClause) {
                result.append(separator).append(target);
                separator = ", ";
            }
        }

        if (havingClause != null) {
            result.append(" having ").append(havingClause);
        }

        if (windowClause != null) {
            String separator = " window ";
            for (WindowDef window : windowClause) {
                result.append(separator).append(window.name).append(" as (").append(window).append(')');
                separator = ", ";
            }
        }

        if (sortClause != null) {
            String separator = " order by ";
            for (Object value : sortClause) {
                result.append(separator).append(value);
                separator = ", ";
            }
        }

        if (limitCount != null) {
            result.append(" limit ").append(limitCount);
        }

        if (limitOffset != null) {
            result.append(" offset ").append(limitOffset);
        }

        if (lockingClause != null) {
            for (LockingClause value : lockingClause) {
                result.append(' ').append(value);
            }
        }

        return result.toString();
    }

    @Override
    public SelectStmt clone() {
        SelectStmt clone = (SelectStmt) super.clone();
        if (distinctClause != null) {
            clone.distinctClause = distinctClause.clone();
        }
        if (intoClause != null) {
            clone.intoClause = intoClause.clone();
        }
        if (targetList != null) {
            clone.targetList = targetList.clone();
        }
        if (fromClause != null) {
            clone.fromClause = fromClause.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (groupClause != null) {
            clone.groupClause = groupClause.clone();
        }
        if (havingClause != null) {
            clone.havingClause = havingClause.clone();
        }
        if (windowClause != null) {
            clone.windowClause = windowClause.clone();
        }
        if (valuesLists != null) {
            clone.valuesLists = valuesLists.clone();
        }
        if (sortClause != null) {
            clone.sortClause = sortClause.clone();
        }
        if (limitOffset != null) {
            clone.limitOffset = limitOffset.clone();
        }
        if (limitCount != null) {
            clone.limitCount = limitCount.clone();
        }
        if (lockingClause != null) {
            clone.lockingClause = lockingClause.clone();
        }
        if (withClause != null) {
            clone.withClause = withClause.clone();
        }
        if (larg != null) {
            clone.larg = larg.clone();
        }
        if (rarg != null) {
            clone.rarg = rarg.clone();
        }
        if (stmt_location != null) {
            clone.stmt_location = stmt_location.clone();
        }
        return clone;
    }

    /**
     * Returns the start offset of the node in the sql statement
     *
     * @return long the start offset of this node or -1 if unknown
     * @since Postgres 18
     */
    @Override
    @XmlTransient
    public final long getStartOffset() {
        if (stmt_location == null || stmt_location.begin == null) {
            return super.getStartOffset();
        }
        return stmt_location.begin.getOffset();
    }

    /**
     * Returns the stmt_len if non zero as String to be represented in an XML structure for debugging purposes
     *
     * @return String
     * @since Postgres 18
     */
    @XmlAttribute(name = "stmtlen")
    public final String getStmtLenString() {
        if (stmt_len == 0) {
            return null;
        }
        return Long.toString(stmt_len);
    }

    /**
     * @return String returns the stmt_location as String to be represented in an XML structure for debugging purposes
     * @since Postgres 18
     */
    @XmlAttribute(name = "stmt_location")
    private String getLocationString() {
        if (stmt_location == null) {
            if (location == null) { // For backward compatibility since Postgres 18
                return null;
            }
            return location.toString();
        }
        return stmt_location.toString();
    }
}
