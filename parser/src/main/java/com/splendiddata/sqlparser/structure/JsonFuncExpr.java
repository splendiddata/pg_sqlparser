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

import com.splendiddata.sqlparser.enums.JsonExprOp;
import com.splendiddata.sqlparser.enums.JsonWrapper;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonFuncExpr -<br>
 * untransformed representation of JSON function expressions
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonFuncExpr extends Node {

    /** expression type */
    @XmlAttribute
    public JsonExprOp op;

    /** common syntax */
    @XmlElement
    public JsonCommon common;

    /** output clause, if specified */
    @XmlElement
    public JsonOutput output;

    /** ON EMPTY behavior, if specified */
    @XmlElement
    public JsonBehavior on_empty;

    /** ON ERROR behavior, if specified */
    @XmlElement
    public JsonBehavior on_error;

    /** array wrapper behavior (JSON_QUERY only) */
    @XmlAttribute
    public JsonWrapper wrapper;

    /** omit or keep quotes? (JSON_QUERY only) */
    @XmlAttribute
    public boolean omit_quotes;

    /**
     * Constructor
     */
    public JsonFuncExpr() {
        super(NodeTag.T_JsonFuncExpr);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonFuncExpr to copy
     */
    public JsonFuncExpr(JsonFuncExpr orig) {
        super(orig);
        this.op = orig.op;
        if (orig.common != null) {
            this.common = orig.common.clone();
        }
        if (orig.output != null) {
            this.output = orig.output.clone();
        }
        if (orig.on_empty != null) {
            this.on_empty = orig.on_empty.clone();
        }
        if (orig.on_error != null) {
            this.on_error = orig.on_error.clone();
        }
        this.wrapper = orig.wrapper;
        this.omit_quotes = orig.omit_quotes;
    }

    @Override
    public JsonFuncExpr clone() {
        JsonFuncExpr clone = (JsonFuncExpr) super.clone();
        if (common != null) {
            clone.common = common.clone();
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
        
        switch (op) {
        case JSON_EXISTS_OP:
            result.append("json_exists(");
            break;
        case JSON_TABLE_OP:
            result.append("json_table(");
            break;
        case JSON_VALUE_OP:
            result.append("json_value(");
            break;
        case JSON_QUERY_OP:
            result.append("json_query(");
            break;
        default:
            result.append("????? please implement op ").append(op.getClass().getName()).append('.').append(op.name())
                    .append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }
        
        if (common != null) {
            result.append(common);
        }

        if (output != null) {
            result.append(" returning ").append(output);
        }
        if (wrapper != null) {
            switch (wrapper) {
            case JSW_NONE:
                break;
            case JSW_CONDITIONAL:
                result.append(" with conditional wrapper");
                break;
            case JSW_UNCONDITIONAL:
                result.append(" with wrapper");
                break;
            default:
                result.append("????? please implement wrapper ").append(wrapper.getClass().getName()).append('.')
                        .append(wrapper.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
                break;
            }
        }

        if (omit_quotes) {
            result.append(" omit quotes");
        }
        
        if (on_empty != null) {
            result.append(' ').append(on_empty).append(" on empty");
        }
        
        if (on_error != null) {
            result.append(' ').append(on_error).append(" on error");
        }
        
        result.append(")");

        return result.toString();
    }
}
