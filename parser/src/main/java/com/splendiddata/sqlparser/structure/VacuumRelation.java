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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from postgresql-11beta4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 6.0 - Postgres version 11
 */
@XmlRootElement(namespace = "parser")
public class VacuumRelation extends Node {
    /** table name to process, or NULL */
    @XmlElement
    public RangeVar relation;

    /** list of column names, or NIL for all */
    @XmlElementWrapper(name = "va_cols")
    @XmlElement(name = "va_col")
    public List<Value> va_cols;

    /**
     * Constructor
     */
    public VacuumRelation() {
        super(NodeTag.T_VacuumRelation);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            The VacuumRelation to copy
     */
    public VacuumRelation(VacuumRelation toCopy) {
        super(toCopy);
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        if (toCopy.va_cols != null) {
            this.va_cols = toCopy.va_cols.clone();
        }
    }

    @Override
    public VacuumRelation clone() {
        VacuumRelation clone = (VacuumRelation) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (va_cols != null) {
            clone.va_cols = va_cols.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (relation != null) {
            result.append(relation);
            if (va_cols != null) {
                result.append('(');
                String separator = "";
                for (Value column : va_cols) {
                    result.append(separator).append(ParserUtil.identifierToSql(column.toString()));
                    separator = ", ";
                }
                result.append(')');
            }
        }

        return result.toString();
    }
}
