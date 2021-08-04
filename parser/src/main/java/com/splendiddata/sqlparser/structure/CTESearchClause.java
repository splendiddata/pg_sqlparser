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
public class CTESearchClause extends Node {

    @XmlElementWrapper(name = "search_col_list")
    @XmlElement(name = "search_col")
    public List<Value> search_col_list;

    @XmlAttribute
    public boolean search_breadth_first;

    @XmlAttribute
    public String search_seq_column;

    /**
     * Constructor
     */
    public CTESearchClause() {
        super(NodeTag.T_CTESearchClause);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CTESearchClause to copy
     */
    public CTESearchClause(CTESearchClause original) {
        super(original);
        if (original.search_col_list != null) {
            this.search_col_list = original.search_col_list.clone();
        }
        this.search_breadth_first = original.search_breadth_first;
        this.search_seq_column = original.search_seq_column;
    }

    @Override
    public CTESearchClause clone() {
        CTESearchClause clone = (CTESearchClause) super.clone();
        if (search_col_list != null) {
            clone.search_col_list = search_col_list.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("search");
        if (search_breadth_first) {
            result.append(" breadth");
        } else {
            result.append(" depth");
        }
        result.append(" first by");
        String separator = " ";
        for (Value column : search_col_list) {
            result.append(separator).append(column);
            separator = ", ";
        }
        if (search_seq_column != null) {
            result.append(" set ").append(search_seq_column);
        }

        return result.toString();
    }
}
