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
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.OnConflictAction;

/**
 * OnConflictClause - representation of ON CONFLICT clause
 * <p>
 * Note: OnConflictClause does not propagate into the Query representation.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class OnConflictClause extends Node {
    /** DO NOTHING or UPDATE? */
    @XmlAttribute
    public OnConflictAction action;

    /** Optional index inference clause */
    @XmlElement
    public InferClause infer;

    /** the target list (of ResTarget) */
    @XmlElementWrapper(name = "targetList")
    @XmlElement(name = "resTarget")
    public List<ResTarget> targetList;

    /** qualifications */
    @XmlElement
    public Node whereClause;

    /**
     * Constructor
     */
    public OnConflictClause() {
        super(NodeTag.T_OnConflictClause);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The OnConflictClause to copy
     */
    public OnConflictClause(OnConflictClause orig) {
        super(orig);
        this.action = orig.action;
        if (orig.infer != null) {
            this.infer = orig.infer.clone();
        }
        if (orig.targetList != null) {
            this.targetList = orig.targetList.clone();
        }
        if (orig.whereClause != null) {
            this.whereClause = orig.whereClause.clone();
        }
    }

    @Override
    public OnConflictClause clone() {
        OnConflictClause clone = (OnConflictClause) super.clone();
        if (infer != null) {
            clone.infer = infer.clone();
        }
        if (targetList != null) {
            clone.targetList = targetList.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        
        switch (action) {
        case ONCONFLICT_NONE:
            break;
        case ONCONFLICT_NOTHING:
            result.append("on conflict ");
            if (infer != null) {
                result.append(infer);
                separator = " ";
            }
            result.append(separator).append("do nothing");
            break;
        case ONCONFLICT_UPDATE:
            result.append("on conflict ");
            if (infer != null) {
                result.append(infer);
                separator = " ";
            }
            result.append(separator).append("do update set ");
            separator = "";
            break;
        default:
            break;

        }

        if (targetList != null) {
            for (ResTarget target : targetList) {
                result.append(separator).append(ParserUtil.identifierToSql(target.name)).append(" = ")
                        .append(target.val);
                separator = ", ";
            }
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        return result.toString();
    }
}
