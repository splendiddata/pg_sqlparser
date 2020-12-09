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
import com.splendiddata.sqlparser.enums.SortByDir;
import com.splendiddata.sqlparser.enums.SortByNulls;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class IndexElem extends Node {

    /** name of attribute to index, or NULL */
    @XmlAttribute
    public String name;

    /** expression to index, or NULL */
    @XmlElement
    public Node expr;

    /**
     * name for index column; NULL = default
     * <p>
     * Not used in the Java implementation, so always null
     * </p>
     */
    @XmlAttribute
    public String indexcolname;

    /** name of collation; NIL = default */
    @XmlElementWrapper(name = "collation")
    @XmlElement(name = "collationNameNode")
    public List<Value> collation;

    /** name of desired opclass; NIL = default */
    @XmlElementWrapper(name = "opclass")
    @XmlElement(name = "opclassNameNode")
    public List<Value> opclass;

    /**
     * opclass-specific options, or NIL
     * 
     * @since 8.0 - Postgres version 13
     */
    @XmlElementWrapper(name = "opclassopts")
    @XmlElement(name = "opclassopt")
    public List<DefElem> opclassopts;

    /** ASC/DESC/default */
    @XmlAttribute
    public SortByDir ordering;

    /** FIRST/LAST/default */
    @XmlAttribute
    public SortByNulls nulls_ordering;

    /**
     * Constructor
     */
    public IndexElem() {
        super(NodeTag.T_IndexElem);
    }

    /**
     * Copy constructor
     *
     * @param orig
     *            The IndexElem to copy
     */
    public IndexElem(IndexElem orig) {
        super(orig);
        this.name = orig.name;
        if (orig.expr != null) {
            this.expr = orig.expr.clone();
        }
        this.indexcolname = orig.indexcolname;
        if (orig.collation != null) {
            this.collation = orig.collation.clone();
        }
        if (orig.opclass != null) {
            this.opclass = orig.opclass.clone();
        }
        this.ordering = orig.ordering;
        this.nulls_ordering = orig.nulls_ordering;
        if (orig.opclassopts != null) {
            this.opclassopts = orig.opclassopts.clone();
        }
    }

    @Override
    public IndexElem clone() {
        IndexElem clone = (IndexElem) super.clone();
        if (expr != null) {
            clone.expr = expr.clone();
        }
        if (collation != null) {
            clone.collation = collation.clone();
        }
        if (opclass != null) {
            clone.opclass = opclass.clone();
        }
        if (opclassopts != null) {
            clone.opclassopts = opclassopts.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (name != null) {
            result.append(ParserUtil.identifierToSql(name));
        } else if (indexcolname != null) {
            result.append(ParserUtil.identifierToSql(indexcolname));
        } else if (expr != null) {
            if (expr instanceof FuncCall) {
                result.append(expr);
            } else {
                result.append('(').append(expr).append(')');
            }
        }

        if (collation != null) {
            result.append(" collate ").append(ParserUtil.nameToSql(collation));
        }

        if (opclass != null) {
            result.append(' ').append(ParserUtil.nameToSql(opclass));
            if (opclassopts != null) {
                String separator = "(";
                for (DefElem opclassopt : opclassopts) {
                    result.append(separator).append(opclassopt.defname);
                    separator = ", ";
                }
                result.append(")");
            }
        }

        if (ordering != null) {
            result.append(ordering);
        }

        if (nulls_ordering != null) {
            result.append(nulls_ordering);
        }

        return result.toString();
    }
}
