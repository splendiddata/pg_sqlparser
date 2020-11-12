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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class CreateSubscriptionStmt extends Node {

    /** Name of of the subscription */
    @XmlAttribute
    public String subname;

    /** Connection string to publisher */
    @XmlAttribute
    public String conninfo;

    /** One or more publication to subscribe to */
    @XmlElementWrapper(name = "publication")
    @XmlElement(name = "publication")
    public List<Value> publication;

    /** List of DefElem nodes */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public CreateSubscriptionStmt() {
        super(NodeTag.T_CreateSubscriptionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreateSubscriptionStmt to copy
     */
    public CreateSubscriptionStmt(CreateSubscriptionStmt original) {
        super(original);
        this.subname = original.subname;
        this.conninfo = original.conninfo;
        if (original.publication != null) {
            this.publication = original.publication.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public CreateSubscriptionStmt clone() {
        CreateSubscriptionStmt clone = (CreateSubscriptionStmt) super.clone();
        if (publication != null) {
            clone.publication = publication.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("create subscription ").append(ParserUtil.identifierToSql(subname))
                .append(" connection '").append(conninfo == null ? "null" : conninfo.replace("'", "''"))
                .append("' publication ");
        String separator = "";
        if (publication != null) {
            for (Value publ : publication) {
                result.append(separator).append(ParserUtil.nameToSql(publ));
                separator = ", ";
            }
        }
        if (options != null && !options.isEmpty()) {
            separator = " with (";
            for (DefElem opt : options) {
                result.append(separator).append(opt.defname);
                if (opt.arg != null) {
                    result.append(" = '").append(opt.arg).append('\'');
                }
                separator = ", ";
            }
            result.append(')');
        }
        return result.toString();
    }
}
