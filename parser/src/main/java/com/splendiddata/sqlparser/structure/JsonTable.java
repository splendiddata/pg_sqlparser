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

import com.splendiddata.sqlparser.enums.JsonTablePlanType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonTable -<br>
 * untransformed representation of JSON_TABLE
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonTable extends Node {

    /** common JSON path syntax fields */
    @XmlElement
    public JsonCommon common;

    /** list of JsonTableColumn */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<JsonTableColumn> columns;

    /** join plan, if specified */
    @XmlElement
    public JsonTablePlan plan;

    /** ON ERROR behavior, if specified */
    @XmlElement
    public JsonBehavior on_error;

    /** table alias in FROM clause */
    @XmlElement
    public Alias alias;

    /** does it have LATERAL prefix? */
    @XmlAttribute
    public boolean lateral;

    /**
     * Constructor
     */
    public JsonTable() {
        super(NodeTag.T_JsonTable);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonTablePlan to copy
     */
    public JsonTable(JsonTable orig) {
        super(orig);
        if (orig.common != null) {
            this.common = orig.common.clone();
        }
        if (orig.columns != null) {
            this.columns = orig.columns.clone();
        }
        if (orig.plan != null) {
            this.plan = orig.plan.clone();
        }
        if (orig.on_error != null) {
            this.on_error = orig.on_error.clone();
        }
        if (orig.alias != null) {
            this.alias = orig.alias.clone();
        }
        this.lateral = orig.lateral;
    }

    @Override
    public JsonTable clone() {
        JsonTable clone = (JsonTable) super.clone();
        if (common != null) {
            clone.common = common.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        if (plan != null) {
            clone.plan = plan.clone();
        }
        if (on_error != null) {
            clone.on_error = on_error.clone();
        }
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (lateral) {
            result.append("lateral ");
        }
        result.append("json_table(");

        if (common != null) {
            result.append(common);
        }

        if (columns != null && !columns.isEmpty()) {
            result.append(" columns ").append(columns);
        }

        if (plan != null) {
            if (JsonTablePlanType.JSTP_SIMPLE.equals(plan.plan_type) && plan.pathname != null) {
                /*
                 * A JsonTable needs parentheses around a plan. But the plan will not provide them if it is a simple
                 * plan with only a pathname.
                 */
                result.append(" plan (").append(plan).append(')');
            } else {
                result.append(" plan ").append(plan);
            }
        }

        if (on_error != null) {
            result.append(' ').append(on_error).append(" on error");
        }

        result.append(')');
        if (alias != null) {
            result.append(" as ").append(alias);
        }

        return result.toString();
    }
}
