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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * GroupingFunc
 * <p>
 * A GroupingFunc is a GROUPING(...) expression, which behaves in many ways like an aggregate function (e.g. it
 * "belongs" to a specific query level, which might not be the one immediately containing it), but also differs in an
 * important respect: it never evaluates its arguments, they merely designate expressions from the GROUP BY clause of
 * the query level to which it belongs.
 * </p>
 * <p>
 * The spec defines the evaluation of GROUPING() purely by syntactic replacement, but we make it a real expression for
 * optimization purposes so that one Agg node can handle multiple grouping sets at once. Evaluating the result only
 * needs the column positions to check against the grouping set being projected. However, for EXPLAIN to produce
 * meaningful output, we have to keep the original expressions around, since expression deparse does not give us any
 * feasible way to get at the GROUP BY clause.
 * </p>
 * <p>
 * Also, we treat two GroupingFunc nodes as equal if they have equal arguments lists and agglevelsup, without comparing
 * the refs and cols annotations.
 * </p>
 * <p>
 * In raw parse output we have only the args list; parse analysis fills in the refs list, and the planner fills in the
 * cols list.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/primnodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class GroupingFunc extends Expr {
    /**
     * arguments, not evaluated but kept for benefit of EXPLAIN etc.
     */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /**
     * Constructor
     */
    public GroupingFunc() {
        super(NodeTag.T_GroupingFunc);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The GroupingFunc to copy
     */
    public GroupingFunc(GroupingFunc orig) {
        super(orig);
        if (orig.args != null) {
            this.args = orig.args.clone();
        }
    }

    @Override
    public GroupingFunc clone() {
        GroupingFunc clone = (GroupingFunc) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("grouping");

        if (args != null) {
            result.append(args);
        }

        return result.toString();
    }
}
