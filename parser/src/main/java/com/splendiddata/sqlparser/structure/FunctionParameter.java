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

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class FunctionParameter extends Node {

    /** parameter name, or NULL if not given */
    @XmlAttribute
    public String name;

    /** TypeName for parameter type */
    @XmlElement
    public TypeName argType;

    /** IN/OUT/etc */
    @XmlAttribute
    public char mode;

    /** raw default expr, or 0 if not given */
    @XmlElement
    public Node defexpr;

    @Override
    public FunctionParameter clone() {
        FunctionParameter clone = (FunctionParameter) super.clone();
        if (argType != null) {
            clone.argType = argType.clone();
        }
        if (defexpr != null) {
            clone.defexpr = defexpr.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (mode) {
        case 'i':
            // in is default
            break;
        case 'o':
            result.append("out ");
            break;
        case 'b':
            result.append("inout ");
            break;
        case 'v':
            result.append("variadic ");
            break;
        case 't':
            // This actually is not a function parameter but the definition of an output column in a resulting table
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("mode", Character.valueOf(mode), getClass()));
            break;
        }

        if (name != null) {
            result.append(ParserUtil.identifierToSql(name)).append(' ');
        }

        if (argType != null) {
            result.append(argType);
        }
        
        if (defexpr != null) {
            result.append(" = ").append(defexpr);
        }
        
        return result.toString();
    }
}
