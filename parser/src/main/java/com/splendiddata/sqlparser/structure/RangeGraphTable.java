/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
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
 * Copied from /postgresql-Postgres 19beta1/src/include/nodes/parsenodes.h
 * <p>
 * RangeGraphTable - raw form of GRAPH_TABLE clause
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class RangeGraphTable extends Node {

    @XmlElement
    public RangeVar graph_name;

    @XmlElement
    public GraphPattern graph_pattern;

    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<ResTarget> columns;

    /** table alias & optional column aliases */
    @XmlElement
    public Alias alias;

    /**
     * Constructor
     */
    public RangeGraphTable() {
        super(NodeTag.T_RangeGraphTable);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            the RangeGraphTable to copy
     */
    public RangeGraphTable(RangeGraphTable original) {
        super(original);

        if (original.graph_name != null) {
            this.graph_name = original.graph_name.clone();
        }
        if (original.graph_pattern != null) {
            this.graph_pattern = original.graph_pattern.clone();
        }
        if (original.columns != null) {
            this.columns = original.columns.clone();
        }
        if (original.alias != null) {
            this.alias = original.alias.clone();
        }
    }

    @Override
    public RangeGraphTable clone() {
        RangeGraphTable clone = (RangeGraphTable) super.clone();
        if (graph_name != null) {
            clone.graph_name = graph_name.clone();
        }
        if (graph_pattern != null) {
            clone.graph_pattern = graph_pattern.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("graph_table(").append(graph_name);
        if (graph_pattern != null) {
            result.append(' ').append(graph_pattern);
        }
        if (columns != null && !columns.isEmpty()) {
            result.append(" columns ").append(columns);
        }
        result.append(')');
        if (alias != null) {
            result.append(' ').append(alias);
        }
        return result.toString();
    }
}
