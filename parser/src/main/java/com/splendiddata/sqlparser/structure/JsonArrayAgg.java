/*
 * Copyright (c) Splendid Data Product Development B.V. 2023
 *
 * This unpublished material is proprietary to Splendid Data Product Development B.V. All rights reserved. The methods
 * and techniques described herein are considered trade secrets and/or confidential. Reproduction or distribution, in
 * whole or in part, is forbidden except by express written permission of Splendid Data Product Development B.V.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayAgg - untransformed representation of JSON_ARRAYAGG()
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayAgg extends Node {

    /** common fields */
    @XmlElement
    public JsonAggConstructor constructor;

    /** array element expression */
    @XmlElement
    public JsonValueExpr arg;

    /* skip NULL elements? */
    @XmlAttribute
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayAgg() {
        super(NodeTag.T_JsonArrayAgg);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArrayAgg(JsonArrayAgg original) {
        super(original);
        if (original.constructor != null) {
            this.constructor = original.constructor.clone();
        }
        if (original.arg != null) {
            this.arg = original.arg.clone();
        }
        this.absent_on_null = original.absent_on_null;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonArrayAgg clone() {
        JsonArrayAgg clone = (JsonArrayAgg) super.clone();
        if (constructor != null) {
            clone.constructor = constructor.clone();
        }
        if (arg != null) {
            clone.arg = arg.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return ("Please implement " + getClass().getName() + ".toString()");
    }
}
