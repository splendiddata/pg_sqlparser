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
 * JsonValueExpr - representation of JSON value expression (expr [FORMAT JsonFormat])
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonValueExpr extends Node {

    /** raw expression */
    @XmlElement
    public Expr raw_expr;

    /** formatted expression or NULL */
    @XmlElement
    public Expr formatted_expr;

    /** FORMAT clause, if specified */
    @XmlElement
    public JsonFormat format;

    /**
     * Constructor
     */
    public JsonValueExpr() {
        super(NodeTag.T_JsonValueExpr);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonValueExpr(JsonValueExpr original) {
        super(original);
        if (original.raw_expr != null) {
            this.raw_expr = original.raw_expr.clone();
        }
        if (original.formatted_expr != null) {
            this.formatted_expr = original.formatted_expr.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonValueExpr clone() {
        JsonValueExpr clone = (JsonValueExpr) super.clone();
        if (raw_expr != null) {
            clone.raw_expr = raw_expr.clone();
        }
        if (formatted_expr != null) {
            clone.formatted_expr = formatted_expr.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (formatted_expr != null) {
            result.append(formatted_expr);
        } else if (raw_expr != null) {
            result.append(raw_expr);
        }
        if (format != null) {
            result.append(format);
        }
        return result.toString();
    }

}
