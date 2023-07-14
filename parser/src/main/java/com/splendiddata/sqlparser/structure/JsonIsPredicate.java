/*
 * Copyright (c) Splendid Data Product Development B.V. 2023
 *
 * This unpublished material is proprietary to Splendid Data Product Development B.V. All rights reserved. The methods
 * and techniques described herein are considered trade secrets and/or confidential. Reproduction or distribution, in
 * whole or in part, is forbidden except by express written permission of Splendid Data Product Development B.V.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.JsonValueType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonIsPredicate - representation of IS JSON predicate
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonIsPredicate extends Node {

    /** subject expression */
    @XmlElement
    public Node expr;

    /** FORMAT clause, if specified */
    @XmlElement
    public JsonFormat format;

    /** JSON item type */
    @XmlAttribute
    public JsonValueType item_type;

    /** check key uniqueness? */
    @XmlAttribute
    public boolean unique_keys;

    public JsonIsPredicate() {
        super(NodeTag.T_JsonIsPredicate);
    }

    public JsonIsPredicate(JsonIsPredicate original) {
        super(original);
        if (original.expr != null) {
            this.expr = original.expr.clone();
        }
        if (original.format != null) {
            this.format = original.format.clone();
        }
        this.item_type = original.item_type;
        this.unique_keys = original.unique_keys;
    }

    public JsonIsPredicate clone() {
        JsonIsPredicate clone = (JsonIsPredicate) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (format != null) {
            clone.format = format.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (expr != null) {
            result.append(expr);
            separator = " ";
        }
        result.append(separator).append("is json");
        separator = " ";
        if (item_type != null) {
            result.append(item_type)
;        }
        if (unique_keys) {
            result.append(separator).append("with unique keys");
        }
        if (format != null) {
            result.append(format);
        }
        return result.toString();
    }
}
