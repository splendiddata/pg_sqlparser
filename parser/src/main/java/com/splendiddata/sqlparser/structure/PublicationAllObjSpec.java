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

import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.PublicationAllObjType;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class PublicationAllObjSpec extends Node {
    
    /** type of this publication object */
    @XmlAttribute
    public PublicationAllObjType pubobjtype;

    /** tables specified in the EXCEPT clause */
    @XmlElementWrapper(name = "except_tables")
    @XmlElement(name = "except_table")
    public List<PublicationObjSpec> except_tables;

    /**
     * Constructor
     */
    public PublicationAllObjSpec() {
        super(NodeTag.T_PublicationAllObjSpec);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The PublicationAllObjSpec to copy
     */
    public PublicationAllObjSpec(PublicationAllObjSpec original) {
        super(original);
        this.pubobjtype = original.pubobjtype;
        if (original.except_tables != null) {
            this.except_tables = original.except_tables.clone();
        }
    }

    @Override
    public PublicationAllObjSpec clone() {
        PublicationAllObjSpec clone = (PublicationAllObjSpec) super.clone();
        if (except_tables != null) {
            clone.except_tables = except_tables.clone();
        }
        return clone;
    }

}
