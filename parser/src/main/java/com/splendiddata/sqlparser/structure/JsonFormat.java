/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
 *
 * This unpublished material is proprietary to Splendid Data Product Development B.V. All rights reserved. The methods
 * and techniques described herein are considered trade secrets and/or confidential. Reproduction or distribution, in
 * whole or in part, is forbidden except by express written permission of Splendid Data Product Development B.V.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.enums.JsonEncoding;
import com.splendiddata.sqlparser.enums.JsonFormatType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonFormat - representation of JSON FORMAT clause
 * <p>
 * Copied from postgresql-16beta1/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgrres 16
 */
@XmlRootElement(namespace = "parser")
public class JsonFormat extends Node {

    /** format type */
    @XmlAttribute
    public JsonFormatType format_type;

    /* JSON encoding */
    @XmlAttribute
    public JsonEncoding encoding;

    /**
     * Constructor
     */
    public JsonFormat() {
        super(NodeTag.T_JsonFormat);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonFormat(JsonFormat original) {
        super(original);
        this.format_type = original.format_type;
        this.encoding = original.encoding;
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     *
     * @return JsonFormat the clone
     */
    @Override
    public JsonFormat clone() {
        return (JsonFormat) super.clone();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Please implement " + getClass().getName() + ".toString()";
    }
}
