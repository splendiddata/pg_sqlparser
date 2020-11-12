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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterSeqStmt extends Node {
    /** the sequence to alter */
    @XmlElement
    public RangeVar sequence;

    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;
    /** skip error if a role is missing? */

    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterSeqStmt() {
        super(NodeTag.T_AlterSeqStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterSeqStmt to copy
     */
    public AlterSeqStmt(AlterSeqStmt original) {
        super(original);
        if (original.sequence != null) {
            this.sequence = original.sequence.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.missing_ok = original.missing_ok;
    }

    @Override
    public AlterSeqStmt clone() {
        AlterSeqStmt clone = (AlterSeqStmt) super.clone();
        if (options != null) {
            clone.options = options.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter sequence ");

        if (missing_ok) {
            result.append("if exists ");
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
                    result.append(' ').append(option.defname);
                    if (option.arg != null) {
                        result.append(' ').append(option.arg);
                    }
                }
            }
        }

        return result.toString();
    }
}
