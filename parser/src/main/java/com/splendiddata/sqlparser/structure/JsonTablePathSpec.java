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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonTablePathSpec
 * <p>
 * untransformed specification of JSON path expression with an optional name
 * <p>
 * Copied from postgresql-17beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonTablePathSpec extends Node {

    @XmlElement
    public Node string;

    @XmlAttribute
    public String name;

    @XmlElement
    public Location name_location;

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
        StringBuilder result = new StringBuilder();
        result.append(string);
        if (name != null) {
            result.append(" as ").append(ParserUtil.identifierToSql(name));
        }
        return result.toString();
    }
}
