/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CreateConversionStmt extends Node {

    /** Name of the conversion */
    @XmlElementWrapper(name = "conversion_name")
    @XmlElement(name = "nameNode")
    public List<Value> conversion_name;

    /** source encoding name */
    @XmlAttribute
    public String for_encoding_name;

    /** destination encoding name */
    @XmlAttribute
    public String to_encoding_name;

    /** qualified conversion function name */
    @XmlElementWrapper(name = "func_name")
    @XmlElement(name = "funcNameNode")
    public List<Value> func_name;

    /** is this a default conversion? */
    @XmlAttribute
    public boolean def;

    /**
     * Constructor
     */
    public CreateConversionStmt() {
        super(NodeTag.T_CreateConversionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateConversionStmt to copy
     */
    public CreateConversionStmt(CreateConversionStmt original) {
        super(original);
        if (original.conversion_name != null) {
            this.conversion_name = original.conversion_name.clone();
        }
        this.for_encoding_name = original.for_encoding_name;
        this.to_encoding_name = original.to_encoding_name;
        if (original.func_name != null) {
            this.func_name = original.func_name.clone();
        }
        this.def = original.def;

    }

    @Override
    public CreateConversionStmt clone() {
        CreateConversionStmt clone = (CreateConversionStmt) super.clone();
        if (conversion_name != null) {
            clone.conversion_name = conversion_name.clone();
        }
        if (func_name != null) {
            clone.func_name = func_name.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (def) {
            result.append("default ");
        }

        result.append("conversion ").append(ParserUtil.nameToSql(conversion_name));

        result.append(" for ").append(ParserUtil.toSqlTextString(for_encoding_name));

        result.append(" to ").append(ParserUtil.toSqlTextString(to_encoding_name));

        result.append(" from ").append(ParserUtil.nameToSql(func_name));

        return result.toString();
    }
}
