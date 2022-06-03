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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonCommon -<br>
 * representation of common syntax of functions using JSON path
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonCommon extends Node {

    /** context item expression */
    @XmlElement
    public JsonValueExpr expr;

    /** JSON path specification expression */
    @XmlElement
    public Node pathspec;

    /** path name, if any */
    @XmlAttribute
    public String pathname;

    /** list of PASSING clause arguments, if any */
    @XmlElementWrapper(name = "passing")
    @XmlElement(name = "passingArgument")
    public List<JsonArgument> passing;

    /**
     * Constructor
     */
    public JsonCommon() {
        super(NodeTag.T_JsonCommon);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonTablePlan to copy
     */
    public JsonCommon(JsonCommon orig) {
        super(orig);
        if (orig.expr != null) {
            this.expr = orig.expr.clone();
        }
        if (orig.pathspec != null) {
            this.pathspec = orig.pathspec.clone();
        }
        this.pathname = orig.pathname;
        if (orig.passing != null) {
            this.passing = orig.passing.clone();
        }
    }

    @Override
    public JsonCommon clone() {
        JsonCommon clone = (JsonCommon) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (pathspec != null) {
            clone.pathspec = pathspec.clone();
        }
        if (passing != null) {
            clone.passing = passing.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String separator = "";
        if (expr != null) {
            result.append(expr);
            separator = ", ";
        }
        if (pathspec != null) {
            result.append(separator).append(pathspec);
            separator = ", ";
        }

        if (pathname != null) {
            result.append(" as ").append(ParserUtil.identifierToSql(pathname));
        }
        if (passing != null) {
            separator = " passing ";
            for (JsonArgument pass : passing) {
                result.append(separator).append(pass);
                separator = ", ";
            }
        }

        return result.toString();
    }
}
