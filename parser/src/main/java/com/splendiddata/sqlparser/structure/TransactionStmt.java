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
import com.splendiddata.sqlparser.enums.TransactionStmtKind;

/**
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class TransactionStmt extends Node {

    @XmlAttribute
    public TransactionStmtKind kind;

    /** for BEGIN/START and savepoint commands */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /*
     * for savepoint commands
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlAttribute
    public String savepoint_name;

    /** for two-phase-commit related commands */
    @XmlAttribute
    public String gid;

    /**
     * AND CHAIN option
     * 
     * @since 7.0 - Postgres 12
     */
    @XmlAttribute
    public boolean chain;

    /**
     * Constructor
     */
    public TransactionStmt() {
        super(NodeTag.T_TransactionStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            to copy
     */
    public TransactionStmt(TransactionStmt original) {
        super(original);
        this.kind = original.kind;
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.savepoint_name = original.savepoint_name;
        this.gid = original.gid;
        this.chain = original.chain;
    }

    @Override
    public TransactionStmt clone() {
        TransactionStmt clone = (TransactionStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        if (options == null && gid == null && savepoint_name == null) {
            return kind.toString();
        }

        StringBuilder result = new StringBuilder();
        result.append(kind);

        if (savepoint_name != null) {
            result.append(' ').append(ParserUtil.identifierToSql(savepoint_name));
        }

        if (options != null) {
            for (DefElem option : options) {
                switch (option.defname) {
                case "transaction_isolation":
                    result.append(" isolation level ").append(((A_Const) option.arg).val.val.str);
                    break;
                case "transaction_read_only":
                    if (((A_Const) option.arg).val.val.ival == 0) {
                        result.append(" read write");
                    } else {
                        result.append(" read only");
                    }
                    break;
                case "transaction_deferrable":
                    if (((A_Const) option.arg).val.val.ival == 0) {
                        result.append(" not deferrable");
                    } else {
                        result.append(" deferrable");
                    }
                    break;
                default:
                    result.append(' ').append(ParserUtil.reportUnknownValue("option", option.defname, getClass()));
                    break;
                }
            }
        }

        if (gid != null) {
            result.append(' ').append(ParserUtil.toSqlTextString(gid));
        }
        
        if (chain) {
            result.append(" and chain");
        }

        return result.toString();
    }
}
