/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class ConstraintsSetStmt extends Node {
    /** List of names as RangeVars */
    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    public List<RangeVar> constraints;

    @XmlAttribute
    public boolean deferred;

    /**
     * Constructor
     */
    public ConstraintsSetStmt() {
        super(NodeTag.T_ConstraintsSetStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ConstraintsSetStmt to copy
     */
    public ConstraintsSetStmt(ConstraintsSetStmt original) {
        super(original);
        if (original.constraints != null) {
            this.constraints = original.constraints.clone();
        }
        this.deferred = original.deferred;
    }

    @Override
    public ConstraintsSetStmt clone() {
        ConstraintsSetStmt clone = (ConstraintsSetStmt) super.clone();
        if (constraints != null) {
            clone.constraints = constraints.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("set constraints");

        if (constraints == null) {
            result.append(" all");
        } else {
            String separator = " ";
            for (RangeVar constraint : constraints) {
                result.append(separator).append(constraint);
                separator = ", ";
            }
        }

        if (deferred) {
            result.append(" deferred");
        } else {
            result.append(" immediate");
        }

        return result.toString();
    }
}
