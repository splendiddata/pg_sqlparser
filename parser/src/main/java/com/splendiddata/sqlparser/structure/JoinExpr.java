/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2021
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

import com.splendiddata.sqlparser.enums.JoinType;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * * JoinExpr - for SQL JOIN expressions
 * <p>
 * isNatural, usingClause, and quals are interdependent. The user can write only one of NATURAL, USING(), or ON() (this
 * is enforced by the grammar). If he writes NATURAL then parse analysis generates the equivalent USING() list, and from
 * that fills in "quals" with the right equality comparisons. If he writes USING() then "quals" is filled with equality
 * comparisons. If he writes ON() then only "quals" is set. Note that NATURAL/USING are not equivalent to ON() since
 * they also affect the output column list.
 * </p>
 * <p>
 * alias is an Alias node representing the AS alias-clause attached to the join expression, or NULL if no clause. NB:
 * presence or absence of the alias has a critical impact on semantics, because a join with an alias restricts
 * visibility of the tables/columns inside it.
 * </p>
 * <p>
 * join_using_alias is an Alias node representing the join correlation name that SQL:2016 and later allow to be attached
 * to JOIN/USING. Its column alias list includes only the common column names from USING, and it does not restrict
 * visibility of the join's input tables.
 * </p>
 * <p>
 * During parse analysis, an RTE is created for the Join, and its index is filled into rtindex. This RTE is present
 * mainly so that Vars can be created that refer to the outputs of the join. The planner sometimes generates JoinExprs
 * internally; these can have rtindex = 0 if there are no join alias variables referencing such joins.
 * </p>
 * <p>
 * Join expression. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class JoinExpr extends Node {

    /** type of join */
    @XmlAttribute
    public JoinType jointype;

    /** Natural join? Will need to shape table */
    @XmlAttribute
    public boolean isNatural;

    /** left subtree */
    @XmlElement
    public Node larg;

    /** right subtree */
    @XmlElement
    public Node rarg;

    /** USING clause, if any (list of String) */
    @XmlElementWrapper(name = "usingClause")
    @XmlElement(name = "using")
    public List<Value> usingClause;

    /**
     * alias attached to USING clause, if any
     * 
     * @since 14.0
     */
    @XmlElement
    public Alias join_using_alias;

    /** qualifiers on join, if any */
    @XmlElement
    public Node quals;

    /** user-written alias clause, if any */
    @XmlElement
    public Alias alias;

    /**
     * Constructor
     */
    public JoinExpr() {
        type = NodeTag.T_JoinExpr;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The JoinExpr to copy
     */
    public JoinExpr(JoinExpr other) {
        super(other);
        this.jointype = other.jointype;
        this.isNatural = other.isNatural;
        if (other.larg != null) {
            this.larg = other.larg.clone();
        }
        if (other.rarg != null) {
            this.rarg = other.rarg.clone();
        }
        if (other.usingClause != null) {
            this.usingClause = other.usingClause.clone();
        }
        if (other.join_using_alias != null) {
            this.join_using_alias = other.join_using_alias.clone();
        }
        if (other.quals != null) {
            this.quals = other.quals.clone();
        }
        if (other.alias != null) {
            this.alias = other.alias.clone();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (alias != null) {
            result.append("(");
        }

        result.append(larg);

        if (isNatural) {
            result.append(" natural");
        }
        switch (jointype) {
        case JOIN_ANTI:
            result.append(" anti");
            break;
        case JOIN_FULL:
            result.append(" full");
            break;
        case JOIN_INNER:
            if (!isNatural && quals == null && usingClause == null) {
                result.append(" cross");
            }
            break;
        case JOIN_LEFT:
            result.append(" left");
            break;
        case JOIN_RIGHT:
            result.append(" right");
            break;
        case JOIN_SEMI:
            result.append(" semi");
            break;
        case JOIN_UNIQUE_INNER:
            result.append(" unique inner");
            break;
        case JOIN_UNIQUE_OUTER:
            result.append(" unique outer");
            break;
        default:
            result.append(" ??? unknown: ").append(jointype).append(" ???");
            break;
        }

        result.append(" join ");

        result.append(rarg);

        if (quals != null) {
            if (quals instanceof A_Expr) {
                result.append(" on (").append(quals).append(')');
            } else {
                result.append(" on ").append(quals);
            }
        } else if (usingClause != null) {
            result.append(" using ").append(usingClause);
            if (join_using_alias != null) {
                result.append(" as ").append(join_using_alias);
            }
        }

        if (alias != null) {
            result.append(") ").append(alias);
        }

        return result.toString();
    }

    @Override
    public JoinExpr clone() {
        JoinExpr clone = (JoinExpr) super.clone();
        if (larg != null) {
            clone.larg = larg.clone();
        }
        if (rarg != null) {
            clone.rarg = rarg.clone();
        }
        if (usingClause != null) {
            clone.usingClause = usingClause.clone();
        }
        if (join_using_alias != null) {
            clone.join_using_alias = join_using_alias.clone();
        }
        if (quals != null) {
            clone.quals = quals.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }
}
