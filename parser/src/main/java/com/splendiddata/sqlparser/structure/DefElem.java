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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.DefElemAction;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class DefElem extends Node {
    /** NULL if unqualified name */
    @XmlAttribute
    public String defnamespace;

    @XmlAttribute
    public String defname;

    /** a (Value *) or a (TypeName *) */
    @XmlElement
    public Node arg;

    /** unspecified action, or SET/ADD/DROP */
    @XmlAttribute
    public DefElemAction defaction;

    /**
     * Constructor
     */
    public DefElem() {
        super(NodeTag.T_DefElem);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The DefElem to copy
     */
    public DefElem(DefElem original) {
        super(original);
        this.defnamespace = original.defnamespace;
        this.defname = original.defname;
        if (original.arg != null) {
            this.arg = original.arg.clone();
        }
        this.defaction = original.defaction;
    }

    @Override
    public DefElem clone() {
        DefElem clone = (DefElem) super.clone();
        if (arg != null) {
            clone.arg = arg.clone();
        }
        return clone;
    }

    /**
     * @throws UnsupportedOperationException
     *             because usages of a DefElem are too versatile to implement properly in the DefElem class
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException(
                "Because DefElem is used very differenty in different classes, the invoking class must implement toString() "
                        + "functionality for a DefElem on an as needed bases in the invoking class itself: "
                        + ParserUtil.stmtToXml(this));
    }
}
