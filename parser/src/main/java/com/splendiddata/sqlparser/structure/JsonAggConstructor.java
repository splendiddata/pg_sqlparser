/*
 * Copyright (c) Splendid Data Product Development B.V. 2023 - 2024
 *
 * This unpublished material is proprietary to Splendid Data Product Development B.V. All rights reserved. The methods
 * and techniques described herein are considered trade secrets and/or confidential. Reproduction or distribution, in
 * whole or in part, is forbidden except by express written permission of Splendid Data Product Development B.V.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonAggConstructor - common fields of untransformed representation of JSON_ARRAYAGG() and JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
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
    public List<Node> agg_order;

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
     * @param original
     *            to copy
     */
    public JsonAggConstructor(JsonAggConstructor original) {
        super(original);
        if (original.output != null) {
            this.output = original.output.clone();
        }
        if (original.agg_filter != null) {
            this.agg_filter = original.agg_filter.clone();
        }
        if (original.agg_order != null) {
            this.agg_order = original.agg_order.clone();
        }
        if (original.over != null) {
            this.over = original.over.clone();
        }

    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
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
        String separator = "";
        if (agg_order != null) {
            result.append("order by");
            separator = " ";
            for (Node node : agg_order) {
                result.append(separator).append(node);
                separator = ", ";
            }
            separator = " ";
        }       
        if (output != null) {
            result.append(separator).append(output);
            separator = " ";
        } 
        if (agg_filter != null) {
            result.append(") filter (where ").append(agg_filter);
            separator = " ";
        }
        if (over != null) {
            result.append(") over (").append(over);
        }
        
        return result.toString();
    }
}
