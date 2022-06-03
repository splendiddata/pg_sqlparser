/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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
 * JsonFormat -<br>
 * representation of JSON FORMAT clause
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/primenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonFormat extends Node {

    /** format type */
    @XmlAttribute
    public JsonFormatType format_type;

    /** JSON encoding */
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
     * @param orig
     *            The JsonFormat to copy
     */
    public JsonFormat(JsonFormat orig) {
        super(orig);
        this.format_type = orig.format_type;
        this.encoding = orig.encoding;
    }

    @Override
    public JsonFormat clone() {
        return (JsonFormat) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String separator = " ";
        switch (format_type) {
        case JS_FORMAT_DEFAULT:
            break;
        case JS_FORMAT_JSON:
            result.append(separator).append("format json");
            break;
        case JS_FORMAT_JSONB:
            result.append(separator).append("format jsonb");
            break;
        default:
            result.append("????? please implement ").append(format_type.getClass().getName()).append('.')
                    .append(format_type.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        switch (encoding) {
        case JS_ENC_DEFAULT:
            break;
        case JS_ENC_UTF16:
            result.append(separator).append("encoding utf16");
            break;
        case JS_ENC_UTF32:
            result.append(separator).append("encoding utf32");
            break;
        case JS_ENC_UTF8:
            result.append(separator).append("encoding utf8");
            break;
        default:
            result.append("????? please implement ").append(encoding.getClass().getName()).append('.')
                    .append(encoding.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        return result.toString();
    }
}
