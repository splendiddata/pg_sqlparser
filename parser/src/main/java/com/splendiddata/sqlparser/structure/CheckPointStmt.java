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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class CheckPointStmt extends Node {

    /**
     * list of DefElem nodes
     * 
     * @since 19beta1
     */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /**
     * Constructor
     */
    public CheckPointStmt() {
        super(NodeTag.T_CheckPointStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CheckPointStmt to copy
     */
    public CheckPointStmt(CheckPointStmt original) {
        super(original);
        if (original.options != null) {
            this.options = original.options.clone();
        }
    }

    @Override
    public CheckPointStmt clone() {
        CheckPointStmt clone = (CheckPointStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("checkpoint");
        if (options != null && !options.isEmpty()) {
            String separator = "(";
            for (DefElem option : options) {
                switch (option.defname) {
                case "flush_unlogged" -> {
                    result.append(separator).append("flush_unlogged");
                    if (option.arg instanceof Value) {
                        result.append(" ").append(option.arg);
                    }
                    separator = ", ";
                }
                case "mode" -> {
                    result.append(separator).append("mode").append(" ").append(option.arg);
                    separator = ", ";
                }
                default -> 
                    result.append(ParserUtil.reportUnknownValue("option", ParserUtil.stmtToXml(option), getClass()));
                }
            }
            result.append(')');
        }
        return result.toString();
    }
}
