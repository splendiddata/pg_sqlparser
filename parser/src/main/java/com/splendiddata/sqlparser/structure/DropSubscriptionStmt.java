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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class DropSubscriptionStmt extends Node {
    /** Name of of the subscription */
    @XmlAttribute
    public String subname;

    /** Skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /** RESTRICT or CASCADE behavior */
    @XmlAttribute
    public DropBehavior behavior;

    /**
     * Constructor
     */
    public DropSubscriptionStmt() {
        super(NodeTag.T_DropSubscriptionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The RawStmt to copy
     */
    public DropSubscriptionStmt(DropSubscriptionStmt original) {
        super(original);
        this.subname = original.subname;
        this.missing_ok = original.missing_ok;
        this.behavior = original.behavior;
    }

    @Override
    public DropSubscriptionStmt clone() {
        return (DropSubscriptionStmt) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("drop subscription ");
        if (missing_ok) {
            result.append("if exists ");
        }
        result.append(subname);
        if (DropBehavior.DROP_CASCADE.equals(behavior)) {
            result.append(" cascade");
        }
        return result.toString();
    }
}
