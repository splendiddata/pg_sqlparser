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
 * CREATE ACCESS METHOD Statement
 * <p>
 * Copied from /postgresql-9.6beta1/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 4.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateAmStmt extends Node {

    /* access method name. */
    public String amname;

    /* handler function name. */
    @XmlElementWrapper(name = "handlerName")
    @XmlElement(name = "handlerNameNode")
    public List<Value> handler_name;

    /**
     * type of access method.
     * 
     * @since 7.0 - Postgres12. Used to be an enum in previous versions
     */
    @XmlAttribute
    public Character amtype;

    /* Copied from /postgresql-12beta2/src/include/calatog/pg_am.h */
    private static final char INDEX_AMTYPE = 'i';
    private static final char TABLE_AMTYPE = 't';
    /**
     * index access method
     * 
     * @since 7.0 - Postgres 12
     */
    public static final Character AMTYPE_INDEX = Character.valueOf(INDEX_AMTYPE);
    /**
     * table access method
     * 
     * @since 7.0 - Postgres 12
     */
    public static final Character AMTYPE_TABLE = Character.valueOf(TABLE_AMTYPE);

    /**
     * Constructor
     */
    public CreateAmStmt() {
        super(NodeTag.T_CreateAmStmt);
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The original
     */
    public CreateAmStmt(CreateAmStmt toCopy) {
        super(toCopy);
        this.amname = toCopy.amname;
        if (toCopy.handler_name != null) {
            this.handler_name = toCopy.handler_name.clone();
        }
        this.amtype = toCopy.amtype;
    }

    @Override
    public CreateAmStmt clone() {
        CreateAmStmt clone = (CreateAmStmt) super.clone();
        if (handler_name != null) {
            clone.handler_name = handler_name.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create access method ");

        result.append(amname);
        if (amtype != null) {
            switch (amtype.charValue()) {
            case INDEX_AMTYPE:
                result.append(" type index");
                break;
            case TABLE_AMTYPE:
                result.append(" type table");
                break;
            default:
                result.append(" ??????? unknown AccessMethodType ").append(amtype).append(" in ")
                        .append(getClass().getName()).append(" ???????");
                break;
            }
        }
        if (handler_name != null) {
            result.append(" handler ").append(ParserUtil.nameToSql(handler_name));
        }
        return result.toString();
    }
}
