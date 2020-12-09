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
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 * @deprecated since 8.0 - Postgres version 13. Use {@link CreateExtensionStmt} instead
 */
@XmlRootElement(namespace = "parser")
@Deprecated(since = "8.0", forRemoval = true)
public class CreatePLangStmt extends Node {
    /** T =&gt; replace if already exists */
    @XmlAttribute
    public boolean replace;

    /** PL name */
    @XmlAttribute
    public String plname;

    /** PL call handler function (qual. name) */
    @XmlElementWrapper(name = "plhandler")
    @XmlElement(name = "plhandlerNameNode")
    public List<Value> plhandler;

    /** optional inline function (qual. name) */
    @XmlElementWrapper(name = "plinline")
    @XmlElement(name = "plinlineNameNode")
    public List<Value> plinline;

    /** optional validator function (qual. name) */
    @XmlElementWrapper(name = "plvalidator")
    @XmlElement(name = "plvalidatorNameNode")
    public List<Value> plvalidator;

    /** PL is trusted */
    @XmlAttribute
    public boolean pltrusted;

    /**
     * Constructor
     */
    public CreatePLangStmt() {
        super(NodeTag.T_CreatePLangStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The CreatePLangStmt to copy
     */
    public CreatePLangStmt(CreatePLangStmt original) {
        this.replace = original.replace;
        this.plname = original.plname;
        if (original.plhandler != null) {
            this.plhandler = original.plhandler.clone();
        }
        if (original.plinline != null) {
            this.plinline = original.plinline.clone();
        }
        if (original.plvalidator != null) {
            this.plvalidator = original.plvalidator.clone();
        }
        this.pltrusted = original.pltrusted;
    }

    @Override
    public CreatePLangStmt clone() {
        CreatePLangStmt clone = (CreatePLangStmt) super.clone();
        if (plhandler != null) {
            clone.plhandler = plhandler.clone();
        }
        if (plinline != null) {
            clone.plinline = plinline.clone();
        }
        if (plvalidator != null) {
            clone.plvalidator = plvalidator.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (replace) {
            result.append("or replace ");
        }

        if (pltrusted) {
            result.append("trusted ");
        }

        result.append("language ").append(ParserUtil.identifierToSql(plname));

        if (plhandler != null) {
            result.append(" handler ").append(ParserUtil.nameToSql(plhandler));
        }

        if (plinline != null) {
            result.append(" inline ").append(ParserUtil.nameToSql(plinline));
        }

        if (plvalidator != null) {
            result.append(" validator ").append(ParserUtil.nameToSql(plvalidator));
        }

        return result.toString();
    }
}
