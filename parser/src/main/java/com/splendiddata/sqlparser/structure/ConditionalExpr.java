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

import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Base class for some conditional expressions
 * <p>
 * Especially A_Expr objects and BoolExpr objects can be intermixed in join conditions (before Postgres release 9.5
 * BoolExpr didn't exist). So this class can be used to put them in Collections alongside each other.
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public abstract class ConditionalExpr extends Expr {
    /**
     * Constructor
     */
    public ConditionalExpr() {
        super();
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The expression to copy
     */
    public ConditionalExpr(ConditionalExpr other) {
        super(other);
    }

    /**
     * Constructor
     *
     * @param type
     *            The type to be used
     */
    protected ConditionalExpr(NodeTag type) {
        super(type);
    }

    @Override
    public ConditionalExpr clone() {
        return (ConditionalExpr) super.clone();
    }
}
