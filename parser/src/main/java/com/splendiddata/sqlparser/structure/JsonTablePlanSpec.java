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

import com.splendiddata.sqlparser.enums.JsonTablePlanJoinType;
import com.splendiddata.sqlparser.enums.JsonTablePlanType;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * JsonTablePlanSpec
 * <p>
 * untransformed specification of JSON path expression with an optional name
 * <p>
 * Copied from postgresql-19beta3/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 19
 */
@XmlRootElement(namespace = "parser")
public class JsonTablePlanSpec extends Node {

    /** plan type */
    @XmlAttribute
    public JsonTablePlanType plan_type;

    /**
     * join type (for joined plan only)
     * <p>
     * The definition of the integer is in {@link JsonTablePlanJoinType}
     */
    @XmlTransient
    public int join_type;

    /* path name (for simple plan only) */
    @XmlAttribute
    public String pathname;

    /* For joined plans */
    /** first joined plan */
    @XmlElement
    public JsonTablePlanSpec plan1;

    /** second joined plan */
    @XmlElement
    public JsonTablePlanSpec plan2;

    /**
     * Constructor
     */
    public JsonTablePlanSpec() {
        super(NodeTag.T_JsonTablePlanSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public JsonTablePlanSpec(JsonTablePlanSpec original) {
        super(original);
        this.plan_type = original.plan_type;
        this.join_type = original.join_type;
        this.pathname = original.pathname;
        if (original.plan1 != null) {
            this.plan1 = original.plan1.clone();
        }
        if (original.plan2 != null) {
            this.plan2 = original.plan2.clone();
        }
    }

    /**
     * @see com.splendiddata.sqlparser.structure.Node#clone()
     */
    @Override
    public JsonTablePlanSpec clone() {
        JsonTablePlanSpec clone = (JsonTablePlanSpec) super.clone();
        if (plan1 != null) {
            clone.plan1 = plan1.clone();
        }
        if (plan2 != null) {
            clone.plan2 = plan2.clone();
        }
        return clone;
    }

    /**
     * Xml work-around to show the ored join_type values
     *
     * @return String representation of join_type
     */
    @XmlAttribute(name = "join_type")
    private String getjoinType() {
        return JsonTablePlanJoinType.toString(join_type);
    }
}
