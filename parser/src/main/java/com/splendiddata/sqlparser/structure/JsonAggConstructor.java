/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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
 * JsonAggConstructor -<br>
 * common fields of untransformed representation of JSON_ARRAYAGG() and JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonAggConstructor extends Node {

    /** RETURNING clause, if any */
    @XmlElement
    public JsonOutput output;

    /** FILTER clause, if any */
    @XmlElement
    public Node agg_filter;

    /** ORDER BY clause, if any */
    @XmlElementWrapper(name = "agg_order")
    @XmlElement(name = "agg_order_element")
    public List<SortBy> agg_order;

    /** OVER clause, if any */
    @XmlElement
    public WindowDef over;

    /**
     * Constructor
     */
    public JsonAggConstructor() {
        super(NodeTag.T_JsonAggConstructor);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonAggConstructor to copy
     */
    public JsonAggConstructor(JsonAggConstructor orig) {
        super(orig);
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
        if (orig.agg_filter != null) {
            this.agg_filter = orig.agg_filter.clone();
        }
        if (orig.agg_order != null) {
            this.agg_order = orig.agg_order.clone();
        }
        if (orig.over != null) {
            this.over = orig.over.clone();
        }
    }

    @Override
    public JsonAggConstructor clone() {
        JsonAggConstructor clone = (JsonAggConstructor) super.clone();
        if (output != null) {
            clone.output = output.clone();
        }
        if (agg_filter != null) {
            clone.agg_filter = agg_filter.clone();
        }
        if (agg_order != null) {
            clone.agg_order = agg_order.clone();
        }
        if (over != null) {
            clone.over = over.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = " ";
        
        if (agg_order != null && ! agg_order.isEmpty()) {
            separator += "order by ";
            for (Node element : agg_order) {
                result.append(separator).append(element);
                separator = ", ";
            }
            separator = " ";
        }
        
        if (output != null) {
            result.append(separator).append("returning ").append(output);
            separator = " ";
        }
        
        result.append(")");
        
        if (agg_filter!=null) {
            result.append(" filter (where ").append(agg_filter).append(')');
        }

        if ( over!=null){
            result.append(" over ").append(over);
        }

        return result.toString();
    }
}
