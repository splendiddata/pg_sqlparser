/*
 * Copyright (c) Splendid Data Product Development B.V. 2025
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

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-18beta1/src/include/utils/elog.h
 * <p>
 * ErrorData holds the data accumulated during any one ereport() cycle. Any non-NULL pointers must point to palloc'd
 * data. (The const pointers are an exception; we assume they point at non-freeable constant strings.)
 * <p>
 *
 * @since Postgres 18.0
 */
@XmlRootElement(namespace = "parser")
public class ErrorData implements Cloneable {
//
// Let's see what is actually used before implementing everything
//
//    /** error level */
//    @XmlAttribute
//    public int elevel;
//
//    /** will report to server log? */
//    @XmlAttribute
//    public boolean output_to_server;
//
//    /** will report to client? */
//    @XmlAttribute
//    public boolean output_to_client;
//
//    /** true to prevent STATEMENT: inclusion */
//    @XmlAttribute
//    public boolean hide_stmt;
//
//    /** true to prevent CONTEXT: inclusion */
//    @XmlAttribute
//    public boolean hide_ctx;
//
//    /** __FILE__ of ereport() call */
//    @XmlAttribute
//    public String filename;
//
//    /** __LINE__ of ereport() call */
//    @XmlAttribute
//    public int lineno;
//
//    /** __func__ of ereport() call */
//    @XmlAttribute
//    public String funcname;
//
//    /** message domain */
//    @XmlAttribute
//    public String domain;
//
//    /** message domain for context message */
//    @XmlAttribute
//    public String context_domain;
//
//    /** encoded ERRSTATE */
//    @XmlAttribute
//    public int sqlerrcode;
//
//    /** primary error message (translated) */
//    @XmlAttribute
//    public String message;
//
//    /** detail error message */
//    @XmlAttribute
//    public String detail;
//
//    /** detail error message for server log only */
//    @XmlAttribute
//    public String detail_log;
//
//    /** hint message */
//    @XmlAttribute
//    public String hint;
//
//    /** context message */
//    @XmlAttribute
//    public String context;
//
//    /** backtrace */
//    @XmlAttribute
//    public String backtrace;
//
//    /** primary message's id (original string) */
//    @XmlAttribute
//    public String message_id;
//
//    /** name of schema */
//    @XmlAttribute
//    public String schema_name;
//
//    /** name of table */
//    @XmlAttribute
//    public String table_name;
//
//    /** name of column */
//    @XmlAttribute
//    public String column_name;
//
//    /** name of datatype */
//    @XmlAttribute
//    public String datatype_name;
//
//    /** name of constraint */
//    @XmlAttribute
//    public String constraint_name;
//
//    /** cursor index into query string */
//    @XmlAttribute
//    public int cursorpos;
//
//    /** cursor index into internalquery */
//    @XmlAttribute
//    public int internalpos;
//
//    /** text of internally-generated query */
//    @XmlAttribute
//    public String internalquery;
//
//    /** errno at entry */
//    @XmlAttribute
//    public int saved_errno;
//
//    /** context containing associated non-constant strings */
//    @XmlElement
//    public MemoryContextData assoc_context;

    /**
     * Constructor
     */
    public ErrorData() {
    }
    
    /**
     * Copy constructor
     *
     * @param original The ErrorData to copy
     */
    public ErrorData(ErrorData original) {
    }
    
    public ErrorData clone() {
        ErrorData clone;
        try {
            clone = (ErrorData) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new ErrorData(this);
        }
        return clone;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("????? please implement ").append(getClass().getName()).append(".toString()");

        return result.toString();
    }
}
