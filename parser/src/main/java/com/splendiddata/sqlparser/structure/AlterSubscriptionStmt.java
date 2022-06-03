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
import com.splendiddata.sqlparser.enums.AlterSubscriptionType;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-10rc1/src/include/nodes/parsenodes.h
 * 
 * @author Splendid Data Product Development B.V.
 * @since 5.0
 */
@XmlRootElement(namespace = "parser")
public class AlterSubscriptionStmt extends Node {
    /** ALTER_SUBSCRIPTION_OPTIONS, etc */
    @XmlAttribute
    public AlterSubscriptionType kind;

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
    public AlterSubscriptionStmt() {
        super(NodeTag.T_AlterSubscriptionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterSubscriptionStmt to copy
     */
    public AlterSubscriptionStmt(AlterSubscriptionStmt original) {
        super(original);
        this.kind = original.kind;
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
    public AlterSubscriptionStmt clone() {
        AlterSubscriptionStmt clone = (AlterSubscriptionStmt) super.clone();
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
        StringBuilder result = new StringBuilder("alter subscription ").append(ParserUtil.identifierToSql(subname));

        String separator = " ";
        switch (kind) {
        case ALTER_SUBSCRIPTION_CONNECTION:
            if (conninfo != null) {
                return result.append(" connection '").append(conninfo.replace("'", "''")).append('\'').toString();
            }
            break;
        case ALTER_SUBSCRIPTION_ENABLED:
            if (options != null && !options.isEmpty()) {
                DefElem option = options.get(0);
                if ("enabled".equals(option.defname) && option.arg instanceof Value
                        && ((Value) option.arg).val.ival != 0) {
                    result.append(" enable");
                } else {
                    result.append(" disable");
                }
            }
            return result.toString();
        case ALTER_SUBSCRIPTION_OPTIONS:
            separator = " set (";
            break;
        case ALTER_SUBSCRIPTION_PUBLICATION:
            separator = " set publication ";
            if (publication != null) {
                for (Value publ : publication) {
                    result.append(separator).append(ParserUtil.nameToSql(publ));
                    separator = ", ";
                }
            }
            separator = " with (";
            break;
        case ALTER_SUBSCRIPTION_REFRESH:
            result.append(" refresh publication");
            separator = " with (";
            break;
        case ALTER_SUBSCRIPTION_SET_PUBLICATION:
            result.append(" set publication");
            if (publication != null) {
                separator = " ";
                for (Value value : publication) {
                    result.append(separator).append(value);
                    separator = ", ";
                }
            }
            separator = " with (";
            break;
        case ALTER_SUBSCRIPTION_ADD_PUBLICATION:
            result.append(" add publication");
            if (publication != null) {
                separator = " ";
                for (Value value : publication) {
                    result.append(separator).append(value);
                    separator = ", ";
                }
            }
            separator = " with (";
            break;
        case ALTER_SUBSCRIPTION_DROP_PUBLICATION:
            result.append(" drop publication");
            if (publication != null) {
                separator = " ";
                for (Value value : publication) {
                    result.append(separator).append(value);
                    separator = ", ";
                }
            }
            separator = " with (";
            break;
        case ALTER_SUBSCRIPTION_SKIP:
            result.append(" skip");
            separator = " (";
            break;
        default:
            return ParserUtil.reportUnknownValue(AlterSubscriptionType.class.getName(), kind, getClass());
        }

        if (options != null && !options.isEmpty()) {
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
