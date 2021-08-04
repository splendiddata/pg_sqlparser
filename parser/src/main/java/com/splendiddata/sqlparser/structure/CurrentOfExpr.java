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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Representing the WHERE current of expression. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CurrentOfExpr extends Node {

    /** name of referenced cursor, or NULL */
    @XmlAttribute
    public String cursor_name;

    /** refcursor parameter number, or 0 */
    @XmlAttribute
    public int cursor_param;

    /**
     * Constructor
     */
    public CurrentOfExpr() {
        super(NodeTag.T_CurrentOfExpr);
    }

    /**
     * Copy constructor
     * 
     * @param original
     *            The CurrentOfExpr to copy
     */
    public CurrentOfExpr(CurrentOfExpr original) {
        super(original);
        this.cursor_name = original.cursor_name;
        this.cursor_param = original.cursor_param;
    }

    @Override
    public CurrentOfExpr clone() {
        return (CurrentOfExpr) super.clone();
    }

    @Override
    public String toString() {
        /*
         * cursor_param will always be zero and has no meaning here. It is used internally be Postgres.
         */
        return "current of " + cursor_name;
    }
}
