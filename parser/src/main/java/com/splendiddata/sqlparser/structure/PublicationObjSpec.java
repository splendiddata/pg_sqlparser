/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.PublicationObjSpecType;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from postgresql-15beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 15
 */
@XmlRootElement(namespace = "parser")
public class PublicationObjSpec extends Node {

    /** type of this publication object */
    @XmlAttribute
    public PublicationObjSpecType pubobjtype = PublicationObjSpecType.PUBLICATIONOBJ_TABLE;

    @XmlAttribute
    public String name;

    @XmlElement
    public PublicationTable pubtable;

    /**
     * Constructor
     */
    public PublicationObjSpec() {
        super(NodeTag.T_PublicationObjSpec);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            The PublicationObjSpec to copy
     */
    public PublicationObjSpec(PublicationObjSpec toCopy) {
        super(toCopy);
        this.pubobjtype = toCopy.pubobjtype;
        this.name = toCopy.name;
        if (toCopy.pubtable != null) {
            this.pubtable = toCopy.pubtable.clone();
        }
    }

    @Override
    public PublicationObjSpec clone() {
        PublicationObjSpec clone = this.clone();
        if (pubtable != null) {
            clone.pubtable = pubtable.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        switch (pubobjtype) {
        case PUBLICATIONOBJ_TABLE:
            return pubtable.toString();
        case PUBLICATIONOBJ_TABLES_IN_CUR_SCHEMA:
            return "tables in schema current_schema";
        case PUBLICATIONOBJ_TABLES_IN_SCHEMA:
            return "tables in schema " + ParserUtil.identifierToSql(name);
        case PUBLICATIONOBJ_CONTINUATION:
        default:
            break;
        }
        return new StringBuilder().append(ParserUtil.reportUnknownValue("pubonjtype", pubobjtype.name(), getClass()))
                .toString();
    }
}
