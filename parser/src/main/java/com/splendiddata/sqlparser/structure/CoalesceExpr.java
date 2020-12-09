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
 * Coalsce expression. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CoalesceExpr extends Expr {
    /** the arguments */
    @XmlElementWrapper(name = "args")
    @XmlElement(name = "arg")
    public List<Node> args;

    /**
     * Constructor
     */
    public CoalesceExpr() {
        super(NodeTag.T_CoalesceExpr);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The CoalesceExpr to copy
     */
    public CoalesceExpr(CoalesceExpr toCopy) {
        super(toCopy);
        if (toCopy.args != null) {
            args = toCopy.args.clone();
        }
    }

    @Override
    public String toString() {
        return "coalesce" + args;
    }

    @Override
    public CoalesceExpr clone() {
        CoalesceExpr clone = (CoalesceExpr) super.clone();
        if (args != null) {
            clone.args = args.clone();
        }
        return clone;
    }
}
