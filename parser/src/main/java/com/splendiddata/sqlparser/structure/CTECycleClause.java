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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-14beta2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 14.0
 */
@XmlRootElement(namespace = "parser")
public class CTECycleClause extends Node {

    @XmlElementWrapper(name = "search_col_list")
    @XmlElement(name = "search_col")
    public List<Value> cycle_col_list;

    @XmlAttribute
    public String cycle_mark_column;

    @XmlElement
    public Node cycle_mark_value;

    @XmlElement
    public Node cycle_mark_default;

    @XmlAttribute
    public String cycle_path_column;

    /**
     * Constructor
     */
    public CTECycleClause() {
        super(NodeTag.T_CTECycleClause);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CTESearchClause to copy
     */
    public CTECycleClause(CTECycleClause original) {
        super(original);
        if (original.cycle_col_list != null) {
            this.cycle_col_list = original.cycle_col_list.clone();
        }
        this.cycle_mark_column = original.cycle_mark_column;
        this.cycle_mark_value = original.cycle_mark_value;
        this.cycle_mark_default = original.cycle_mark_default;
        this.cycle_path_column = original.cycle_path_column;
    }

    @Override
    public CTECycleClause clone() {
        CTECycleClause clone = (CTECycleClause) super.clone();
        if (cycle_col_list != null) {
            clone.cycle_col_list = cycle_col_list.clone();
        }
        if (cycle_mark_value != null) {
            clone.cycle_mark_value = cycle_mark_value.clone();
        }
        if (cycle_mark_default != null) {
            clone.cycle_mark_default = cycle_mark_default.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("cycle");
        if (cycle_col_list != null) {
            String separator = " ";
            for (Value col : cycle_col_list) {
                result.append(separator).append(col);
                separator = ", ";
            }
        }
        result.append(" set ").append(cycle_mark_column);
        if (cycle_mark_value != null) {
            result.append(" to ");
            if (cycle_mark_value instanceof TypeCast) {
                result.append(((TypeCast)cycle_mark_value).type).append(" ").append(((TypeCast)cycle_mark_value).arg);
            } else {
                result.append(cycle_mark_value);
            }
            if (cycle_mark_default != null) {
                result.append(" default ");
                if (cycle_mark_default instanceof TypeCast) {
                    result.append(((TypeCast)cycle_mark_default).type).append(" ").append(((TypeCast)cycle_mark_default).arg);
                } else {
                    result.append(cycle_mark_default);
                }
            }
        }
        if (cycle_path_column != null) {
            result.append(" using ").append(cycle_path_column);
        }
        return result.toString();
    }
}
