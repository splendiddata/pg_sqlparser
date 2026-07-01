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

import com.splendiddata.sqlparser.enums.AlterPropGraphElementKind;
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-Postgres 19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19beta1
 */
@XmlRootElement(namespace = "parser")
public class AlterPropGraphStmt extends Node {

    @XmlElement
    public RangeVar pgname;

    @XmlAttribute
    public boolean missing_ok;

    @XmlElementWrapper(name = "add_vertex_tables")
    @XmlElement(name = "add_vertex_table")
    public List<Node> add_vertex_tables; // TODO: Make more precise

    @XmlElementWrapper(name = "add_edge_tables")
    @XmlElement(name = "add_edge_table")
    public List<Node> add_edge_tables; // TODO: Make more precise

    @XmlElementWrapper(name = "drop_vertex_tables")
    @XmlElement(name = "drop_vertex_table")
    public List<Node> drop_vertex_tables; // TODO: Make more precise

    @XmlElementWrapper(name = "drop_edge_tables")
    @XmlElement(name = "drop_edge_table")
    public List<Node> drop_edge_tables; // TODO: Make more precise

    @XmlAttribute
    public DropBehavior drop_behavior;

    @XmlAttribute
    public AlterPropGraphElementKind element_kind;

    @XmlAttribute
    public String element_alias;

    @XmlElementWrapper(name = "add_labels")
    @XmlElement(name = "add_label")
    public List<PropGraphLabelAndProperties> add_labels;

    @XmlAttribute
    public String drop_label;

    @XmlAttribute
    public String alter_label;

    @XmlElement
    public PropGraphProperties add_properties;

    @XmlElementWrapper(name = "drop_properties")
    @XmlElement(name = "drop_propertie")
    public List<Node> drop_properties; // TODO: Make more precise

    /**
     * Constructor
     */
    public AlterPropGraphStmt() {
        super(NodeTag.T_AlterPropGraphStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterPropGraphStmt to copy
     */
    public AlterPropGraphStmt(AlterPropGraphStmt original) {
        super(original);
        if (original.pgname != null) {
            this.pgname = original.pgname.clone();
        }
        this.missing_ok = original.missing_ok;
        if (original.add_vertex_tables != null) {
            this.add_vertex_tables = original.add_vertex_tables.clone();
        }
        if (original.add_edge_tables != null) {
            this.add_edge_tables = original.add_edge_tables.clone();
        }
        if (original.drop_vertex_tables != null) {
            this.drop_vertex_tables = original.drop_vertex_tables.clone();
        }
        if (original.drop_edge_tables != null) {
            this.drop_edge_tables = original.drop_edge_tables.clone();
        }
        this.drop_behavior = original.drop_behavior;
        this.element_kind = original.element_kind;
        this.element_alias = original.element_alias;
        if (original.add_labels != null) {
            this.add_labels = original.add_labels.clone();
        }
        this.drop_label = original.drop_label;
        this.alter_label = original.alter_label;
        if (original.add_properties != null) {
            this.add_properties = original.add_properties.clone();
        }
        if (original.drop_properties != null) {
            this.drop_properties = original.drop_properties.clone();
        }
    }

    @Override
    public AlterPropGraphStmt clone() {
        AlterPropGraphStmt clone = (AlterPropGraphStmt) super.clone();
        if (pgname != null) {
            clone.pgname = pgname.clone();
        }
        if (add_vertex_tables != null) {
            clone.add_vertex_tables = add_vertex_tables.clone();
        }
        if (add_edge_tables != null) {
            clone.add_edge_tables = add_edge_tables.clone();
        }
        if (drop_vertex_tables != null) {
            clone.drop_vertex_tables = drop_vertex_tables.clone();
        }
        if (drop_edge_tables != null) {
            clone.drop_edge_tables = drop_edge_tables.clone();
        }
        if (add_labels != null) {
            clone.add_labels = add_labels.clone();
        }
        if (add_properties != null) {
            clone.add_properties = add_properties.clone();
        }
        if (drop_properties != null) {
            clone.drop_properties = drop_properties.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("alter property graph ");
        if (missing_ok) {
            result.append("if exists ");
        }
        if (pgname != null) {
            result.append(pgname);
        }
        if (add_vertex_tables != null && !add_vertex_tables.isEmpty()) {
            result.append(" add vertex tables ").append(add_vertex_tables);
        }
        if (add_edge_tables != null && !add_edge_tables.isEmpty()) {
            result.append(" add edge tables ").append(add_edge_tables);
        }
        if (drop_vertex_tables != null && !drop_vertex_tables.isEmpty()) {
            result.append(" drop vertex tables ").append(drop_vertex_tables);
        }
        if (drop_edge_tables != null && !drop_edge_tables.isEmpty()) {
            result.append(" drop edge tables ").append(drop_edge_tables);
        }
        if (element_kind != null) {
            result.append(" alter ").append(element_kind);
        }
        if (element_alias != null) {
            result.append(" table ").append(element_alias);
        }
        if (add_labels != null && !add_labels.isEmpty()) {
            for (Node label : add_labels) {
                result.append(" add ").append(label);
            }
        }
        if (drop_label != null) {
            result.append(" drop label ").append(drop_label);
        }
        if (alter_label != null) {
            result.append(" alter label ").append(alter_label);
        }
        if (add_properties != null) {
            result.append(" add ").append(add_properties);
        }
        if (drop_properties != null) {
            result.append(" drop properties ").append(drop_properties);
        }
        if (drop_behavior != null) {
            switch (drop_behavior) {
            case DROP_CASCADE -> result.append(" cascade");
            case DROP_RESTRICT -> result.append(" restrict");
            default -> {
            }
            }
        }

        return result.toString();
    }
}
