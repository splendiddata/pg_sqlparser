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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonArgument -<br>
 * representation of argument from JSON PASSING clause
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
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
     * @param orig
     *            The JsonArgument to copy
     */
    public JsonArgument(JsonArgument orig) {
        super(orig);
        if (orig.val != null) {
            this.val = orig.val.clone();
        }
        this.name = orig.name;
    }

    @Override
    public JsonArgument clone() {
        JsonArgument clone = (JsonArgument) super.clone();
        if (val != null) {
            clone.val = val.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(val);
        if (name != null) {
            result.append(" as ").append(ParserUtil.identifierToSql(name));
        }

        return result.toString();
    }
}
