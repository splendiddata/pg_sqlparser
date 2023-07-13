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
 * JsonKeyValue - untransformed representation of JSON object key-value pair for JSON_OBJECT() and JSON_OBJECTAGG()
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonKeyValue extends Node {

    /** key expression */
    @XmlElement
    public Expr key;

    /** JSON value expression */
    @XmlElement
    public JsonValueExpr value;

    /**
     * Constructor
     */
    public JsonKeyValue() {
        super(NodeTag.T_JsonKeyValue);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonKeyValue(JsonKeyValue original) {
        super(original);
        if (original.key != null) {
            this.key = original.key.clone();
        }
        if (original.value != null) {
            this.value = original.value.clone();
        }
    }

    @Override
    public JsonKeyValue clone() {
        JsonKeyValue clone = (JsonKeyValue) super.clone();
        if (key != null) {
            clone.key = key.clone();
        }
        if (value != null) {
            clone.value = value.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (key != null) {
            result.append(key);
        }
        result.append(": ");
        if (value == null) {
            result.append("null");
        } else {
            result.append(value);
        }
        return result.toString();
    }
}
