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

import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * InferClause - ON CONFLICT unique index inference clause
 * <p>
 * Note: InferClause does not propagate into the Query representation.
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class InferClause extends Node {
    /** IndexElems to infer unique index */
    @XmlElementWrapper(name = "indexElems")
    @XmlElement(name = "indexElem")
    public List<IndexElem> indexElems;

    /** qualification (partial-index predicate) */
    @XmlElement
    public Node whereClause;

    /** Constraint name, or NULL if unnamed */
    @XmlAttribute
    public String conname;

    /**
     * Constructor
     */
    public InferClause() {
        super(NodeTag.T_InferClause);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            the InferClause to copy
     */
    public InferClause(InferClause orig) {
        super(orig);
        if (orig.indexElems != null) {
            this.indexElems = orig.indexElems.clone();
        }
        if (orig.whereClause != null) {
            this.whereClause = orig.whereClause.clone();
        }
        this.conname = orig.conname;
    }

    @Override
    public InferClause clone() {
        InferClause clone = (InferClause) super.clone();
        if (indexElems != null) {
            clone.indexElems = indexElems.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        String leadingSpace = "";
        if (conname != null) {
            result.append("on constraint ").append(conname);
            leadingSpace = " ";
        }

        if (indexElems != null) {
            result.append(leadingSpace).append(indexElems);
            leadingSpace = " ";
        }

        if (whereClause != null) {
            result.append(leadingSpace).append("where ").append(whereClause);
        }

        return result.toString();
    }
}
