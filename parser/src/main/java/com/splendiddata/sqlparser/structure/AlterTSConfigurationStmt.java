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
import com.splendiddata.sqlparser.enums.AlterTSConfigType;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class AlterTSConfigurationStmt extends Node {
    /** ALTER_TSCONFIG_ADD_MAPPING, etc */
    @XmlAttribute
    public AlterTSConfigType kind;

    /** qualified name (list of Value strings) */
    @XmlElementWrapper(name = "cfgname")
    @XmlElement(name = "cfgNameNode")
    public List<Value> cfgname;

    /** list of Value strings */
    @XmlElementWrapper(name = "tokentype")
    @XmlElement(name = "tokenTypeNode")
    public List<Value> tokentype;

    /** list of list of Value strings */
    @XmlElementWrapper(name = "dicts")
    @XmlElement(name = "dict")
    public List<List<Value>> dicts;

    /** if true - remove old variant */
    @XmlAttribute
    public boolean override;

    /** if true - replace dictionary by another */
    @XmlAttribute
    public boolean replace;

    /** for DROP - skip error if missing? */
    @XmlAttribute
    public boolean missing_ok;

    /**
     * Constructor
     */
    public AlterTSConfigurationStmt() {
        super(NodeTag.T_AlterTSConfigurationStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The AlterTSConfigurationStmt to copy
     */
    public AlterTSConfigurationStmt(AlterTSConfigurationStmt original) {
        super(original);
        this.kind = original.kind;
        if (original.cfgname != null) {
            this.cfgname = original.cfgname.clone();
        }
        if (original.tokentype != null) {
            this.tokentype = original.tokentype.clone();
        }
        if (original.dicts != null) {
            this.dicts = original.dicts.clone();
        }
        this.override = original.override;
        this.replace = original.replace;
        this.missing_ok = original.missing_ok;
    }

    @Override
    public AlterTSConfigurationStmt clone() {
        AlterTSConfigurationStmt clone = (AlterTSConfigurationStmt) super.clone();
        if (cfgname != null) {
            clone.cfgname = cfgname.clone();
        }
        if (tokentype != null) {
            clone.tokentype = tokentype.clone();
        }
        if (dicts != null) {
            clone.dicts = dicts.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("alter text search configuration ");

        result.append(ParserUtil.nameToSql(cfgname));

        if (tokentype == null) {
            result.append(" alter mapping");
        } else {
            if (dicts == null) {
                result.append(" drop mapping");
                if (missing_ok) {
                    result.append(" if exists");
                }
            } else if (replace) {
                result.append(" alter mapping");
            } else if (override) {
                result.append(" alter mapping");
            } else {
                result.append(" add mapping");
            }
            String separator = " for ";
            for (Value token : tokentype) {
                result.append(separator).append(token);
                separator = ", ";
            }
        }

        if (dicts != null) {
            if (replace) {
                String separator = " replace ";
                for (List<Value> dict : dicts) {
                    result.append(separator).append(ParserUtil.nameToSql(dict));
                    separator = " with ";
                }
            } else {
                String separator = " with ";
                for (List<Value> dict : dicts) {
                    result.append(separator).append(ParserUtil.nameToSql(dict));
                    separator = ", ";
                }
            }
        }

        return result.toString();
    }
}
