/*
 * Copyright (c) Splendid Data Product Development B.V. 2025
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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-18beta1/src/include/nodes/parsenodes.h
 * <p>
 * ReturningClause - List of RETURNING expressions, together with any WITH(...) options
 *
 * @author Splendid Data Product Development B.V.
 * @since 18.0
 */
@XmlRootElement(namespace = "parser")
public class ReturningClause extends Node {

    /** list of ReturningOption elements */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<ReturningOption> options;

    /** list of expressions to return */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<Expr> exprs;

    /**
     * Constructor
     */
    public ReturningClause() {
        super(NodeTag.T_ReturningClause);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ReturningClause to copy
     */
    public ReturningClause(ReturningClause original) {
        super(original);
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.exprs != null) {
            this.exprs = original.exprs.clone();
        }
    }

    @Override
    public ReturningClause clone() {
        ReturningClause clone = (ReturningClause) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        if (exprs != null) {
            clone.exprs = exprs.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("returning");
        String separator = " ";
        if (options != null && !options.isEmpty()) {
            separator = " with (";
            for (ReturningOption option : options) {
                result.append(separator).append(option);
                separator = ", ";
            }
            result.append(')');
            separator = " ";
        }
        if (exprs != null && !exprs.isEmpty()) {
            for (Expr expr : exprs) {
                result.append(separator).append(expr);
                separator = ", ";
            }
            separator = " ";
        }

        return result.toString();
    }
}
