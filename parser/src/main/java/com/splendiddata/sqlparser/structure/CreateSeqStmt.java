/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * <p>
 * ----------------------<br>
 * {Create|Alter} SEQUENCE Statement<br>
 * ----------------------
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class CreateSeqStmt extends Node {

    /** the sequence to create */
    @XmlElement
    public RangeVar sequence;

    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** ID of owner, or InvalidOid for default */
    @XmlElement
    public Oid ownerId;

    /** just do nothing if it already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /**
     * Constructor
     */
    public CreateSeqStmt() {
        super(NodeTag.T_CreateSeqStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            Ths CreateSeqStmt to copy
     */
    public CreateSeqStmt(CreateSeqStmt original) {
        super(original);
        if (original.sequence != null) {
            this.sequence = original.sequence.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        if (original.ownerId != null) {
            this.ownerId = original.ownerId.clone();
        }
        this.if_not_exists = original.if_not_exists;

    }

    @Override
    public CreateSeqStmt clone() {
        CreateSeqStmt clone = (CreateSeqStmt) super.clone();
        if (sequence != null) {
            clone.sequence = sequence.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        if (ownerId != null) {
            clone.ownerId = ownerId.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create");

        switch (sequence.relpersistence) {
        case RELPERSISTENCE_PERMANENT:
            break;
        case RELPERSISTENCE_TEMP:
            result.append(" temporary");
            break;
        case RELPERSISTENCE_UNLOGGED:
            result.append(" unlogged");
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("RangeVar.sequence", sequence.relpersistence, getClass()));
        }

        result.append(" sequence ");

        if (if_not_exists) {
            result.append("if not exists ");
        }

        result.append(sequence);

        if (options != null) {
            for (DefElem option : options) {
                switch (option.defname) {
                case "cycle":
                    if (((Value) option.arg).val.ival == 0) {
                        result.append(" no");
                    }
                    result.append(' ').append(option.defname);
                    break;
                case "owned_by":
                    result.append(" owned by ");
                    if (((List<Value>) option.arg).size() == 1
                            && "none".equals(((List<Value>) option.arg).get(0).val.str)) {
                        result.append("none");
                    } else {
                        result.append(ParserUtil.nameToSql(option.arg));
                    }
                    break;
                case "minvalue":
                case "maxvalue":
                    if (option.arg == null) {
                        result.append(" no ").append(option.defname);
                    } else {
                        result.append(' ').append(option.defname).append(' ').append(option.arg);
                    }
                    break;
                default:
                    result.append(' ').append(option.defname).append(' ').append(option.arg);
                }
            }
        }

        return result.toString();
    }
}
