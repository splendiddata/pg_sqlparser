/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.enums;

/**
 * Sort by direction, based on /postgresql-9.3.4/src/include/nodes/primnodes.h#XmlExprOp
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */

public enum XmlExprOp {
    /** XMLCONCAT(args) */
    IS_XMLCONCAT("xmlconcat"),
    /** XMLELEMENT(name, xml_attributes, args) */
    IS_XMLELEMENT("xmlelement"),
    /** XMLFOREST(xml_attributes) */
    IS_XMLFOREST("xmlforest"),
    /** XMLPARSE(text, is_doc, preserve_ws) */
    IS_XMLPARSE("xmlparse"),
    /** XMLPI(name [, args]) */
    IS_XMLPI("xmlpi"),
    /** XMLROOT(xml, version, standalone) */
    IS_XMLROOT("xmlroot"),
    /** XMLSERIALIZE(is_document, xmlval) */
    IS_XMLSERIALIZE("xmlserialize"),
    /** xmlval IS DOCUMENT */
    IS_DOCUMENT("xmldocument");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    private final String toString;

    /**
     * Constructor
     *
     * @param toString
     */
    private XmlExprOp(String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (XmlExprOp type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
