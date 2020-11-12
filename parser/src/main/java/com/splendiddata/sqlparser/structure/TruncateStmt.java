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

import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class TruncateStmt extends Node {

    /** relations (RangeVars) to be truncated */
    @XmlElementWrapper(name = "relations")
    @XmlElement(name = "relation")
    public List<RangeVar> relations;

    /** restart owned sequences? */
    @XmlAttribute
    public boolean restart_seqs;

    /** RESTRICT or CASCADE behavior */
    @XmlAttribute
    public DropBehavior behavior;

    /**
     * Constructor
     */
    public TruncateStmt() {
        super(NodeTag.T_TruncateStmt);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The TruncateStmt to copy
     */
    public TruncateStmt(TruncateStmt orig) {
        if (orig.relations != null) {
            this.relations = orig.relations.clone();
        }
        this.restart_seqs = orig.restart_seqs;
        this.behavior = orig.behavior;
    }

    @Override
    public TruncateStmt clone() {
        TruncateStmt clone = (TruncateStmt) super.clone();
        if (relations != null) {
            clone.relations = relations.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("truncate");

        String separator = " ";
        for (RangeVar relation : relations) {
            result.append(separator).append(relation);
            separator = ", ";
        }

        if (restart_seqs) {
            result.append(" restart identity");
        }

        result.append(behavior);

        return result.toString();
    }
}
