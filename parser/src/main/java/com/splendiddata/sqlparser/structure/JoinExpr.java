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

import com.splendiddata.sqlparser.enums.JoinType;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Join expression. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
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
        this.larg = other.larg;
        this.rarg = other.rarg;
        this.usingClause = other.usingClause;
        this.quals = other.quals;
        this.alias = other.alias;
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
        if (quals != null) {
            clone.quals = quals.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }
}
