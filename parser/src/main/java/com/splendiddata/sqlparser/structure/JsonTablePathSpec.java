/*
 * Copyright (c) Splendid Data Product Development B.V. 2023 - 2024
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
 * JsonTablePathSpec<p>
 *      untransformed specification of JSON path expression with an optional
 *      name
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonTablePathSpec extends Node {
    
@XmlElement
    public Node       string;

@XmlAttribute
    public String       name;

@XmlElement
    public Location    name_location;

    /**
     * Constructor
     */
    public JsonTablePathSpec() {
        super(NodeTag.T_JsonTablePathSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonTablePathSpec(JsonTablePathSpec original) {
        super(original);
        if (original.string != null) {
            this.string = original.string.clone();
        }
        this.name = original.name;
        if (original.name_location != null) {
            this.name_location = original.name_location.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonTablePathSpec clone() {
        JsonTablePathSpec clone = (JsonTablePathSpec) super.clone();
        if (string != null) {
            clone.string = string.clone();
        }
        if (name_location != null) {
            clone.name_location = name_location.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return "????? Please implement " + this.getClass().getName() + ".toString() ?????";
    }
}
