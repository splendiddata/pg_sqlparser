/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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
import com.splendiddata.sqlparser.enums.JsonExprOp;
import com.splendiddata.sqlparser.enums.JsonQuotes;
import com.splendiddata.sqlparser.enums.JsonWrapper;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonFuncExpr
 * <p>
 * untransformed representation of function expressions for SQL/JSON query functions
 * 
 * <p>
 * Join expression. Copied from /postgresql-17beta1/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since Postgres 17
 */
@XmlRootElement(namespace = "parser")
public class JsonFuncExpr extends Expr {

    /** expression type */
    @XmlAttribute
    public JsonExprOp op;

    /**
     * JSON_TABLE() column name or NULL if this is not for a JSON_TABLE()
     */
    @XmlAttribute
    public String column_name;

    /** context item expression */
    @XmlElement
    public JsonValueExpr context_item;

    /** JSON path specification expression */
    @XmlElement
    public Node pathspec;

    /** list of PASSING clause arguments, if any */
    @XmlElementWrapper(name = "passingList")
    @XmlElement(name = "passingElement")
    public List<Node> passing;

    /** output clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** ON EMPTY behavior */
    @XmlElement
    public JsonBehavior on_empty;

    /** ON ERROR behavior */
    @XmlElement
    public JsonBehavior on_error;

    /** array wrapper behavior (JSON_QUERY only) */
    @XmlAttribute
    public JsonWrapper wrapper;

    /** omit or keep quotes? (JSON_QUERY only) */
    @XmlAttribute
    public JsonQuotes quotes;

    /**
     * Constructor
     */
    public JsonFuncExpr() {
        type = NodeTag.T_JsonFuncExpr;
    }

    /**
     * Shallow copy constructor
     *
     * @param other
     *            The JoinExpr to copy
     */
    public JsonFuncExpr(JsonFuncExpr other) {
        super(other);
        this.op = other.op;
        this.column_name = other.column_name;
        if (other.context_item != null) {
            this.context_item = other.context_item.clone();
        }
        if (other.pathspec != null) {
            this.pathspec = other.pathspec.clone();
        }
        if (other.passing != null) {
            this.passing = other.passing.clone();
        }
        if (other.output != null) {
            this.output = other.output.clone();
        }
        if (other.on_empty != null) {
            this.on_empty = on_empty.clone();
        }
        if (other.on_error != null) {
            this.on_error = on_error.clone();
        }
        this.wrapper = other.wrapper;
        this.quotes = other.quotes;
    }

    @Override
    public JsonFuncExpr clone() {
        JsonFuncExpr clone = (JsonFuncExpr) super.clone();
        if (context_item != null) {
            clone.context_item = context_item.clone();
        }
        if (pathspec != null) {
            clone.pathspec = pathspec.clone();
        }
        if (passing != null) {
            clone.passing = passing.clone();
        }
        if (output != null) {
            clone.output = output.clone();
        }
        if (on_empty != null) {
            clone.on_empty = on_empty.clone();
        }
        if (on_error != null) {
            clone.on_error = on_error.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (op != null) {
            result.append(op);
        }
        result.append('(');
        if (column_name != null) {
            result.append(separator).append(ParserUtil.reportUnknownValue("column_name", column_name, getClass()));
            separator = " ";
        }
        if (context_item != null) {
            result.append(separator).append(context_item);
            separator = " ";
        }
        if (pathspec != null) {
            result.append(", ").append(pathspec);
            separator = " ";
        }
        if (passing != null) {
            separator += "passing ";
            for (Node pass : passing) {
                result.append(separator).append(pass);
                separator = ", ";
            }
            separator = " ";
        }
        if (output != null) {
            result.append(separator).append(output);
            separator = " ";
        }
        if (wrapper != null) {
            switch (wrapper) {
            case JSW_CONDITIONAL:
                result.append(separator).append("with conditional wrapper");
                break;
            case JSW_NONE:
                result.append(separator).append("without wrapper");
                break;
            case JSW_UNCONDITIONAL:
                result.append(separator).append("with unconditional wrapper");
                break;
            case JSW_UNSPEC:
                break;
            default:
                result.append(separator).append(ParserUtil.reportUnknownValue("wrapper", wrapper, getClass()));
                separator = " ";
                break;
            }
        }
        if (quotes != null) {
            switch (quotes) {
            case JS_QUOTES_KEEP:
                result.append(separator).append("keep quotes");
                separator = " ";
                break;
            case JS_QUOTES_OMIT:
                result.append(separator).append("omit quotes");
                separator = " ";
                break;
            case JS_QUOTES_UNSPEC:
                break;
            default:
                result.append(separator).append(ParserUtil.reportUnknownValue("quotes", quotes, getClass()));
                break;
            }
        }
        if (on_empty != null) {
            result.append(separator).append(on_empty).append(" on empty");
            separator = " ";
        }
        if (on_error != null) {
            result.append(separator).append(on_error).append(" on error");
            separator = " ";
        }
        result.append(')');

        return result.toString();
    }
}
