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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArrayConstructor - untransformed representation of JSON_ARRAY(element,...) constructor
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonArrayConstructor extends Node {

    /** list of JsonValueExpr elements */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<JsonValueExpr> exprs;

    /** RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** skip NULL elements? */
    @XmlAttribute
    public boolean absent_on_null;

    /**
     * Constructor
     */
    public JsonArrayConstructor() {
        super(NodeTag.T_JsonArrayConstructor);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArrayConstructor(JsonArrayConstructor original) {
        super(original);
        if (original.exprs != null) {
            this.exprs = original.exprs.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonArrayConstructor clone() {
        JsonArrayConstructor clone = (JsonArrayConstructor) super.clone();
        if (exprs != null) {
            clone.exprs = exprs.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return ("Please implement " + getClass().getName() + ".toString()");
    }
}
