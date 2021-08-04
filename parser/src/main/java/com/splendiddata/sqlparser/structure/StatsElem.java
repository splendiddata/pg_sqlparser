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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * StatsElem - statistics parameters (used in CREATE STATISTICS)
 * <p>
 * For a plain attribute, 'name' is the name of the referenced table column and 'expr' is NULL. For an expression,
 * 'name' is NULL and 'expr' is the expression tree.
 * </p>
 * <p>
 * copied from: /postgresql-14beta2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 14.0
 */
@XmlRootElement(namespace = "parser")
public class StatsElem extends Node {

    /** name of attribute to index, or NULL */
    @XmlAttribute
    public String name;

    /** expression to index, or NULL */
    @XmlElement
    public Node expr;

    /**
     * Constructor
     */
    public StatsElem() {
        super(NodeTag.T_StatsElem);
    }

    /**
     * Copy constructor
     * 
     * @param original
     *            The StatsElem to copy
     */
    public StatsElem(StatsElem original) {
        super(NodeTag.T_StatsElem);
        this.name = original.name;
        if (original.expr != null) {
            this.expr = original.expr.clone();
        }
    }

    @Override
    public StatsElem clone() {
        StatsElem clone = (StatsElem) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (expr != null) {
            return new StringBuilder().append("(").append(expr).append(")").toString();
        }
        return name;
    }
}