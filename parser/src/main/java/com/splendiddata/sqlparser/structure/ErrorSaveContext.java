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

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-18beta1/src/include/nodes/miscnodes.h
 * <p>
 * 
 * ErrorSaveContext - function call context node for handling of "soft" errors
 * <p>
 *
 * A caller wishing to trap soft errors must initialize a struct like this with all fields zero/NULL except for the
 * NodeTag. Optionally, set details_wanted = true if more than the bare knowledge that a soft error occurred is
 * required. The struct is then passed to a SQL-callable function via the FunctionCallInfo.context field; or below the
 * level of SQL calls, it could be passed to a subroutine directly.
 * <p>
 *
 * After calling code that might report an error this way, check error_occurred to see if an error happened. If so, and
 * if details_wanted is true, error_data has been filled with error details (stored in the callee's memory context!).
 * The ErrorData can be modified (e.g. downgraded to a WARNING) and reported with ThrowErrorData(). FreeErrorData() can
 * be called to release error_data, although that step is typically not necessary if the called code was run in a
 * short-lived context.
 * <p>
 *
 * @since Postgres 18.0
 */
@XmlRootElement(namespace = "parser")
public class ErrorSaveContext extends Node {

    /** set to true if we detect a soft error */
    @XmlAttribute
    public boolean error_occurred;

    /** does caller want more info than that? */
    @XmlAttribute
    public boolean details_wanted;

    /** details of error, if so */
    @XmlElement
    public ErrorData error_data;

    /**
     * Constructor
     */
    public ErrorSaveContext() {
        super(NodeTag.T_ErrorSaveContext);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The ErrorSaveContext to copy
     */
    public ErrorSaveContext(ErrorSaveContext original) {
        super(original);
        this.error_occurred = original.error_occurred;
        this.details_wanted = original.details_wanted;
        if (original.error_data != null) {
            this.error_data = original.error_data.clone();
        }
    }

    @Override
    public ErrorSaveContext clone() {
        ErrorSaveContext clone = (ErrorSaveContext) super.clone();
        if (error_data != null) {
            clone.error_data = error_data.clone();
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
