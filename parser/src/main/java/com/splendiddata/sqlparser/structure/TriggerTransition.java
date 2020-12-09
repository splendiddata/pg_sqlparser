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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * TriggerTransition - representation of transition row or table naming clause
 * <p>
 * Only transition tables are initially supported in the syntax, and only for AFTER triggers, but other permutations are
 * accepted by the parser so we can give a meaningful message from C code.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class TriggerTransition extends Node {
    /** name of column to partition on, or NULL */
    @XmlAttribute
    public String name;

    @XmlAttribute
    public boolean isNew;

    @XmlAttribute
    public boolean isTable;

    /**
     * Constructor
     */
    public TriggerTransition() {
        super(NodeTag.T_TriggerTransition);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The TriggerTransition to copy
     */
    public TriggerTransition(TriggerTransition original) {
        super(original);
        this.name = original.name;
        this.isNew = original.isNew;
        this.isTable = original.isTable;
    }

    @Override
    public TriggerTransition clone() {
        return (TriggerTransition) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (isNew) {
            result.append("new ");
        } else {
            result.append("old ");
        }
        result.append("table as ").append(name);

        return result.toString();
    }
}
