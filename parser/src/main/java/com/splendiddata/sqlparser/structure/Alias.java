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

/**
 * Alias names. Copied from /postgresql-9.3.4/src/include/nodes/primnodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class Alias extends Node {

    /** aliased rel name (never qualified) */
    @XmlAttribute
    public String aliasname;

    /** optional list of column aliases */
    @XmlElementWrapper(name = "colnames")
    @XmlElement(name = "colname")
    public List<Value> colnames;

    /**
     * Constructor
     */
    public Alias() {
        super(NodeTag.T_Alias);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The Alias to copy
     */
    public Alias(Alias original) {
        this.aliasname = original.aliasname;
        if (original.colnames != null) {
            this.colnames = original.colnames.clone();
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(ParserUtil.identifierToSql(aliasname)).append(ParserUtil.argumentsToSql(colnames))
                .toString();
    }

    @Override
    public Alias clone() {
        Alias clone = (Alias) super.clone();
        if (colnames != null) {
            clone.colnames = colnames.clone();
        }
        return clone;
    }
}
