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

package com.splendiddata.sqlparser.structure;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.XmlOptionType;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class XmlSerialize extends Node {
    /**
     * DOCUMENT or CONTENT
     */
    @XmlAttribute
    public XmlOptionType xmloption;

    @XmlElement
    public Node expr;

    @XmlElement
    public TypeName typeName;

    @Override
    public XmlSerialize clone() {
        XmlSerialize clone = (XmlSerialize) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("xmlserialize(");

        if (xmloption != null) {
            switch (xmloption) {
            case XMLOPTION_DOCUMENT:
                result.append("document ");
                break;
            case XMLOPTION_CONTENT:
                result.append("content ");
                break;
            default:
                result.append(ParserUtil.reportUnknownValue("xmloption", xmloption, getClass()));
                break;
            }
        }
        result.append(expr);

        if (typeName != null) {
            result.append(" as ").append(typeName);
        }

        result.append(')');

        return result.toString();
    }
}
