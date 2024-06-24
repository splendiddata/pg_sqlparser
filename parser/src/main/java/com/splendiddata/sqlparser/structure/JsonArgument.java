/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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
 * JsonArgument - representation of argument from JSON PASSING clause
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonArgument extends Node {

    /** argument value expression */
    @XmlElement
    public JsonValueExpr val;
    
    /** argument name */
    @XmlAttribute
    public String name;

    /**
     * Constructor
     */
    public JsonArgument() {
        super(NodeTag.T_JsonArgument);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonArgument(JsonArgument original) {
        super(original);
        if (original.val != null) {
            this.val = original.val.clone();
        }
        this.name = original.name;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     *
     * @return JsonFormat the clone
     */
    @Override
    public JsonArgument clone() {
        JsonArgument clone = (JsonArgument) super.clone();
        if (val != null) {
            clone.val = val.clone();
        }
        return clone;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "????? Please implement " + this.getClass().getName() + ".toString() ?????";
    }
}
