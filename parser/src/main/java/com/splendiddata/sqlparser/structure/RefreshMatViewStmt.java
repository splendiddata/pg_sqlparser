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
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 2.1.0
 */
@XmlRootElement(namespace = "parser")
public class RefreshMatViewStmt extends Node {
    /** allow concurrent access? */
    @XmlAttribute
    public boolean concurrent;

    /** true for WITH NO DATA */
    @XmlAttribute
    public boolean skipData;

    /** relation to insert into */
    @XmlElement
    public RangeVar relation;

    /**
     * Constructor
     */
    public RefreshMatViewStmt() {
        super(NodeTag.T_RefreshMatViewStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The RefreshMatViewStmt to copy
     */
    public RefreshMatViewStmt(RefreshMatViewStmt original) {
        super(original);
        this.concurrent = original.concurrent;
        this.skipData = original.skipData;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
    }

    @Override
    public RefreshMatViewStmt clone() {
        RefreshMatViewStmt clone = (RefreshMatViewStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("refresh materialized view ");

        if (concurrent) {
            result.append("concurrently ");
        }

        result.append(relation);

        if (skipData) {
            result.append(" with no data");
        }

        return result.toString();
    }
}
