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
 * CREATE PROPERTY GRAPH Statement
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class CreatePropGraphStmt extends Node {

    @XmlElement
    public RangeVar pgname;

    @XmlElementWrapper(name = "vertex_tables")
    @XmlElement(name = "vertex_table")
    public List<PropGraphVertex> vertex_tables;

    @XmlElementWrapper(name = "edge_tables")
    @XmlElement(name = "edge_table")
    public List<PropGraphEdge> edge_tables;

    /**
     * Constructor
     *
     */
    public CreatePropGraphStmt() {
        super(NodeTag.T_CreatePropGraphStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreatePropGraphStmt to copy
     */
    public CreatePropGraphStmt(CreatePropGraphStmt original) {
        super(original);
        if (original.pgname != null) {
            this.pgname = original.pgname.clone();
        }
        if (original.vertex_tables != null) {
            this.vertex_tables = original.vertex_tables.clone();
        }
        if (original.edge_tables != null) {
            this.edge_tables = original.edge_tables.clone();
        }
    }

    @Override
    public CreatePropGraphStmt clone() {
        CreatePropGraphStmt clone = (CreatePropGraphStmt) super.clone();
        if (pgname != null) {
            clone.pgname = pgname.clone();
        }
        if (vertex_tables != null) {
            clone.vertex_tables = vertex_tables.clone();
        }
        if (edge_tables != null) {
            clone.edge_tables = edge_tables.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("create property graph");
        if (pgname != null) {
            result.append(' ').append(pgname);
        }
        if (vertex_tables != null && !vertex_tables.isEmpty()) {
            result.append(" vertex tables ").append(vertex_tables);
        }
        if (edge_tables != null && !edge_tables.isEmpty()) {
            result.append(" edge tables ").append(edge_tables);
        }

        return result.toString();
    }
}
