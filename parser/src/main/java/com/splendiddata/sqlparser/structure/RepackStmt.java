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
import com.splendiddata.sqlparser.enums.RepackCommand;

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
public class RepackStmt extends Node {

    /** type of command being run */
    @XmlAttribute
    public RepackCommand command;

    /** relation being repacked */
    @XmlElement
    public VacuumRelation relation;

    /** order tuples by this index */
    @XmlAttribute
    public String indexname;

    /** whether USING INDEX is specified */
    @XmlAttribute
    public boolean usingindex;

    /** list of DefElem nodes */
    @XmlElementWrapper(name = "params")
    @XmlElement(name = "param")
    public List<DefElem> params;

    /**
     * Constructor
     */
    public RepackStmt() {
        super(NodeTag.T_RepackStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The RepackStmt to copy
     */
    public RepackStmt(RepackStmt original) {
        super(original);
        this.command = original.command;
        if (original.relation != null) {
            this.relation = original.relation.clone();
        }
        this.indexname = original.indexname;
        this.usingindex = original.usingindex;
        if (original.params != null) {
            this.params = original.params.clone();
        }
    }

    @Override
    public RepackStmt clone() {
        RepackStmt clone = (RepackStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (params != null) {
            clone.params = params.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (command != null) {
            switch (command) {
            case REPACK_COMMAND_CLUSTER -> result.append("cluster");
            case REPACK_COMMAND_REPACK -> result.append("repack");
            case REPACK_COMMAND_VACUUMFULL -> result.append("vacuum");
            default -> result.append(ParserUtil.reportUnknownValue("command", command, getClass()));
            }
        }
        if (params != null && !params.isEmpty()) {
            String separator = " (";
            for (DefElem param : params) {
                result.append(separator).append(param.defname);
                separator = ", ";
                if (param.arg != null) {
                    result.append(' ').append(param.arg);
                }
            }
            result.append(')');
        }
        if (relation != null) {
            result.append(" ").append(relation);
        }
        if (command != null) {
            switch (command) {
            case REPACK_COMMAND_CLUSTER -> {
                if (indexname != null) {
                    result.append(" using ").append(indexname);
                }
            }
            case REPACK_COMMAND_REPACK -> {
                if (usingindex) {
                    result.append(" using index ");
                }
                if (indexname != null) {
                    result.append(' ').append(indexname);
                }
            }
            case REPACK_COMMAND_VACUUMFULL -> {
            }
            default -> result.append(ParserUtil.reportUnknownValue("command", command, getClass()));
            }
        }
        return result.toString();
    }
}
