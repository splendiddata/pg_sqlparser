/*
 * Copyright (c) Splendid Data Product Development B.V. 2023
 *
 * This unpublished material is proprietary to Splendid Data Product Development B.V. All rights reserved. The methods
 * and techniques described herein are considered trade secrets and/or confidential. Reproduction or distribution, in
 * whole or in part, is forbidden except by express written permission of Splendid Data Product Development B.V.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayQueryConstructor - untransformed representation of JSON_ARRAY(subquery) constructor
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayQueryConstructor extends Node {

    /** subquery */
    @XmlElement
    public Node query;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** FORMAT clause for subquery, if specified */
    @XmlElement
    public JsonFormat format;

    /** skip NULL elements? */
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayQueryConstructor() {
        super(NodeTag.T_JsonArrayQueryConstructor);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArrayQueryConstructor(JsonArrayQueryConstructor original) {
        super(original);
        if (original.query != null) {
            this.query = original.query.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
        this.absent_on_null = original.absent_on_null;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonArrayQueryConstructor clone() {
        JsonArrayQueryConstructor clone = (JsonArrayQueryConstructor) super.clone();
        if (query != null) {
            clone.query = query.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return ("Please implement " + getClass().getName() + ".toString()");
    }
}
