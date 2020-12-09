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
     * Constructor
     */
    public SelectStmt() {
        super(NodeTag.T_SelectStmt);
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The SelectStmt to copy
     */
    public SelectStmt(SelectStmt other) {
        super(other);
        this.distinctClause = other.distinctClause;
        this.intoClause = other.intoClause;
        this.targetList = other.targetList;
        this.fromClause = other.fromClause;
        this.whereClause = other.whereClause;
        this.groupClause = other.groupClause;
        this.havingClause = other.havingClause;
        this.windowClause = other.windowClause;
        this.valuesLists = other.valuesLists;
        this.sortClause = other.sortClause;
        this.limitOffset = other.limitOffset;
        this.limitCount = other.limitCount;
        this.limitOption = other.limitOption;
        this.lockingClause = other.lockingClause;
        this.withClause = other.withClause;
        this.op = other.op;
        this.all = other.all;
        this.larg = other.larg;
        this.rarg = other.rarg;
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
                result.append(separator).append(window.name).append(" as ").append(window);
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
        return clone;
    }
}
