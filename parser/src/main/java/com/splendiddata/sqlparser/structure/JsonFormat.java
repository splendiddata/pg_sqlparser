/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
 *
 * This program is free software: You may redistribute and/or modify under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at Client's option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, Client should
 * obtain one via www.gnu.org/licenses/.
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
 * @since Postgres 16
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
        StringBuilder result = new StringBuilder("");
        if (format_type != null) {
            result.append(format_type);
        }
        if (encoding != null && !"".equals(encoding.toString())) {
            result.append(" ").append(encoding);
        }
        return result.toString();
    }
}
