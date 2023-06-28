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
 * JsonObjectConstructor - untransformed representation of JSON_OBJECT() constructor
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonObjectConstructor extends Node {

    /* list of JsonKeyValue pairs */
    @XmlElementWrapper(name = "exprs")
    @XmlElement(name = "expr")
    public List<JsonKeyValue> exprs;

    /* RETURNING clause, if specified */
    @XmlElement
    public JsonOutput output;

    /* skip NULL values? */
    @XmlAttribute
    public boolean absent_on_null;

    /** check key uniqueness? */
    @XmlAttribute
    public boolean unique;

    /**
     * Constructor
     */
    public JsonObjectConstructor() {
        super(NodeTag.T_JsonObjectConstructor);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonObjectConstructor(JsonObjectConstructor original) {
        super(original);
        if (original.exprs != null) {
            this.exprs = original.exprs.clone();
        }
        if (original.output != null) {
            this.output = original.output.clone();
        }
        this.absent_on_null = original.absent_on_null;
        this.unique = original.unique;
    }

    @Override
    public JsonObjectConstructor clone() {
        JsonObjectConstructor clone = (JsonObjectConstructor) super.clone();
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
