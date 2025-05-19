/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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
import com.splendiddata.sqlparser.enums.DropBehavior;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class AlterDomainStmt extends Node {
    /**
     * ------------
     * <ul>
     * <li>T = alter column default</li>
     * <li>N = alter column drop not null</li>
     * <li>O = alter column set not null</li>
     * <li>C = add constraint</li>
     * <li>X = drop constraint</li>
     * <li>V = validate constraint</li>
     * </ul>
     * ------------
     */
    @XmlAttribute
    public char subtype;

    /** domain to work on */
    @XmlElementWrapper(name = "typeName")
    @XmlElement(name = "nameNode")
    public List<Value> typeName;

    /** skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /** column or constraint name to act on */
    @XmlAttribute
    public String name;

    /** RESTRICT or CASCADE for DROP cases */
    @XmlAttribute
    public DropBehavior behavior;

    /** definition of default or constraint */
    @XmlElement
    public Node def;

    /**
     * Constructor
     */
    public AlterDomainStmt() {
        super(NodeTag.T_AlterDomainStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            Ths AlterDomainStmt to copy
     */
    public AlterDomainStmt(AlterDomainStmt original) {
        super(original);
        this.subtype = original.subtype;
        if (original.typeName != null) {
            this.typeName = original.typeName.clone();
        }
        this.missing_ok = original.missing_ok;
        this.name = original.name;
        this.behavior = original.behavior;
        if (original.def != null) {
            this.def = original.def.clone();
        }
    }

    @Override
    public AlterDomainStmt clone() {
        AlterDomainStmt clone = (AlterDomainStmt) super.clone();
        if (typeName != null) {
            clone.typeName = typeName.clone();
        }
        if (def != null) {
            clone.def = def.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter domain ").append(ParserUtil.nameToSql(typeName));

        switch (subtype) {
        case 'T':
            if (def == null) {
                result.append(" drop default");
            } else {
                result.append(" set default ").append(def);
            }
            break;
        case 'N':
            result.append(" drop not null");
            break;
        case 'O':
            result.append(" set not null");
            break;
        case 'C':
            result.append(" add ").append(def);
            break;
        case 'X':
            result.append(" drop constraint");
            if (missing_ok) {
                result.append(" if exists");
            }
            result.append(' ').append(name);
            if (behavior != null) {
                switch (behavior) {
                case DROP_CASCADE:
                    result.append(" cascade");
                    break;
                case DROP_RESTRICT:
                    result.append(" restrict");
                    break;
                default:
                    result.append(ParserUtil.reportUnknownValue("behavior", behavior, getClass()));
                    break;
                }
            }
            break;
        case 'V':
            result.append(" validate constraint ").append(ParserUtil.identifierToSql(name));
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("subtype", subtype, getClass()));
            break;
        }

        return result.toString();
    }
}
