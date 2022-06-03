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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
@XmlRootElement(namespace = "parser")
public class PublicationTable extends Node {

    /** relation to be published */
    @XmlElement
    public RangeVar relation;

    /** qualifications */
    @XmlElement
    public Node whereClause;

    /** List of columns in a publication table */
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    public List<Value> columns;

    /**
     * Constructor
     */
    public PublicationTable() {
        super(NodeTag.T_PublicationTable);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            The PublicationTable to copy
     */
    public PublicationTable(PublicationTable toCopy) {
        super(toCopy);
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        if (toCopy.whereClause != null) {
            this.whereClause = toCopy.whereClause.clone();
        }
        if (toCopy.columns != null) {
            this.columns = toCopy.columns.clone();
        }
    }

    @Override
    public PublicationTable clone() {
        PublicationTable clone = this.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (columns != null) {
            clone.columns = columns.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("table ");
        result.append(relation);
        if (columns != null && ! columns.isEmpty()) {
            String sep = "(";
            for (Value column : columns) {
                result.append(sep).append(column);
                sep = ", ";
            }
            result.append(')');
        }
        if (whereClause != null) {
            result.append(" where (").append(whereClause).append(')');
        }
        return result.toString();
    }
}
