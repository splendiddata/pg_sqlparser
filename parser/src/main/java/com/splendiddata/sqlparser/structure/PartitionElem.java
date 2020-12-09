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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * PartitionElem - parse-time representation of a single partition key
 * <p>
 * expr can be either a raw expression tree or a parse-analyzed expression. We don't store these on-disk, though.
 * </p>
 * <p>
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class PartitionElem extends Node {
    /** name of column to partition on, or NULL */
    @XmlAttribute
    public String name;

    /** expression to partition on, or NULL */
    @XmlElement
    public Node expr;

    /** name of collation; NIL = default */
    @XmlTransient
    public List<Node> collation;

    /** name of desired opclass; NIL = default */
    @XmlTransient
    public List<Node> opclass;

    /**
     * Constructor
     */
    public PartitionElem() {
        super(NodeTag.T_PartitionElem);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public PartitionElem(PartitionElem original) {
        super(original);
        this.name = original.name;
        if (original.expr != null) {
            this.expr = original.expr.clone();
        }
        if (original.collation != null) {
            this.collation = original.collation.clone();
        }
        if (original.opclass != null) {
            this.opclass = original.opclass.clone();
        }
    }

    @Override
    public PartitionElem clone() {
        PartitionElem clone = (PartitionElem) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (collation != null) {
            clone.collation = collation.clone();
        }
        if (opclass != null) {
            clone.opclass = opclass.clone();
        }
        return clone;
    }

    /**
     * Returns the qualified name of the collation for debugging purposes
     *
     * @return String the qualified name of the collation if specified
     */
    @XmlAttribute(name = "collation")
    private String getCollationQualifiedName() {
        if (collation == null) {
            return null;
        }
        return ParserUtil.nameToSql(collation);
    }

    /**
     * Returns the qualified name of the operator class for debugging purposes
     *
     * @return String the qualified name of the opclass if specified
     */
    @XmlAttribute(name = "opclass")
    private String getOpClassQualifiedName() {
        if (opclass == null) {
            return null;
        }
        return ParserUtil.nameToSql(opclass);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";
        if (name != null) {
            result.append(name);
            separator = " ";
        }
        if (expr != null) {
            result.append(separator).append('(').append(expr).append(')');
            separator = " ";
        }
        if (collation != null) {
            result.append(separator).append("collate ").append(getCollationQualifiedName());
            separator = " ";
        }
        if (opclass != null) {
            result.append(separator).append(getOpClassQualifiedName());
        }
        return result.toString();
    }
}
