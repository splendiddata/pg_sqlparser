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
import com.splendiddata.sqlparser.enums.JsonTablePlanJoinType;
import com.splendiddata.sqlparser.enums.JsonTablePlanType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * JsonFormat -<br>
 * representation of JSON FORMAT clause
 * <p>
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 15.0
 */
@XmlRootElement(namespace = "parser")
public class JsonTablePlan extends Node {

    /** plan type */
    @XmlAttribute
    public JsonTablePlanType plan_type;

    /**
     * join type (for joined plan only)
     * <p>
     * The result of (some of) JsonTablePlanJoinType.JSTPJ_INNER, JsonTablePlanJoinType.JSTPJ_OUTER,
     * JsonTablePlanJoinType.JSTPJ_CROSS, JsonTablePlanJoinType.JSTPJ_UNION ORed together
     */
    @XmlAttribute
    public int join_type;

    /** first joined plan */
    @XmlElement
    public JsonTablePlan plan1;

    /** second joined plan */
    @XmlElement
    public JsonTablePlan plan2;

    /** path name (for simple plan only) */
    @XmlAttribute
    public String pathname;

    /**
     * Constructor
     */
    public JsonTablePlan() {
        super(NodeTag.T_JsonTablePlan);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The JsonTablePlan to copy
     */
    public JsonTablePlan(JsonTablePlan orig) {
        super(orig);
        this.plan_type = orig.plan_type;
        this.join_type = orig.join_type;
        if (orig.plan1 != null) {
            this.plan1 = orig.plan1.clone();
        }
        if (orig.plan2 != null) {
            this.plan2 = orig.plan2.clone();
        }
        this.pathname = orig.pathname;
    }

    @Override
    public JsonTablePlan clone() {
        JsonTablePlan clone = (JsonTablePlan) super.clone();
        if (plan1 != null) {
            clone.plan1 = plan1.clone();
        }
        if (plan2 != null) {
            clone.plan2 = plan2.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        switch (plan_type) {
        case JSTP_DEFAULT:
            result.append("default (");
            addJoinType(result);
            result.append(')');
            break;
        case JSTP_JOINED:
            result.append("(");
            if (plan1 != null) {
                result.append(plan1).append(' ');
            }
            addJoinType(result);
            if (plan2 != null) {
                result.append(' ').append(plan2);
            }
            result.append(')');
            break;
        case JSTP_SIMPLE:
            if (pathname != null) {
                result.append(ParserUtil.identifierToSql(pathname));
            } else if (plan1 != null) {
                result.append(plan1);
            }
            break;
        default:
            result.append("????? please implement plan_type ").append(plan_type.getClass().getName()).append('.')
                    .append(plan_type.name()).append(" in ").append(getClass().getName()).append(".toString() ?????");
            break;
        }

        return result.toString();
    }

    private void addJoinType(StringBuilder result) {
        String separator = "";
        if ((join_type & JsonTablePlanJoinType.JSTPJ_INNER) == JsonTablePlanJoinType.JSTPJ_INNER) {
            result.append("inner");
            separator = ", ";
        }
        if ((join_type & JsonTablePlanJoinType.JSTPJ_OUTER) == JsonTablePlanJoinType.JSTPJ_OUTER) {
            result.append(separator).append("outer");
            separator = ", ";
        }
        if ((join_type & JsonTablePlanJoinType.JSTPJ_CROSS) == JsonTablePlanJoinType.JSTPJ_CROSS) {
            result.append(separator).append("cross");
            separator = ", ";
        }
        if ((join_type & JsonTablePlanJoinType.JSTPJ_UNION) == JsonTablePlanJoinType.JSTPJ_UNION) {
            result.append(separator).append("union");
            separator = ", ";
        }
    }
}
