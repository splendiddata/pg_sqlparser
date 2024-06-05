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
 * JsonOutput - representation of JSON output clause (RETURNING type [FORMAT format])
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonOutput extends Node {

    /** RETURNING type name, if specified */
    @XmlElement
    public TypeName typeName;

    /** RETURNING FORMAT clause and type Oids */
    @XmlElement
    public JsonReturning returning;

    /**
     * Constructor
     */
    public JsonOutput() {
        super(NodeTag.T_JsonOutput);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonOutput(JsonOutput original) {
        super(original);
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        if (original.returning != null) {
            this.returning = original.returning.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonOutput clone() {
        JsonOutput clone = (JsonOutput) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (returning != null) {
            clone.returning = returning.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("returning ");
        String separator = "";
        if (typeName != null) {
            result.append(typeName);
            separator = " ";
        }
        if (returning != null) {
            result.append(separator).append(returning);
        }
        return result.toString();
    }
}
