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
 * JsonReturning - transformed representation of JSON RETURNING clause
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonReturning extends Node {

    /** output JSON format */
    @XmlElement
    public JsonFormat format;

    /** target type Oid */
    @XmlAttribute
    public Oid typid;

    /** target type modifier */
    @XmlAttribute
    public int typmod;

    /**
     * Constructor
     */
    public JsonReturning() {
        super(NodeTag.T_JsonReturning);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonReturning(JsonReturning original) {
        super(original);
        if (original.format != null) {
            this.format = original.format.clone();
        }
        if (original.typid != null) {
            this.typid = original.typid.clone();
        }
        this.typmod = original.typmod;

    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonReturning clone() {
        JsonReturning clone = (JsonReturning) super.clone();
        if (format != null) {
            clone.format = format.clone();
        }
        if (typid != null) {
            clone.typid = typid.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        return ("Please implement " + getClass().getName() + ".toString()");
    }
}
