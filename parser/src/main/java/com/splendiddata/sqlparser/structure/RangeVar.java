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
import javax.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.InhOption;
import com.splendiddata.sqlparser.enums.NodeTag;
import com.splendiddata.sqlparser.enums.RelPersistence;

/**
 * RangeVar - range variable, used in FROM clauses
 * <p>
 * Also used to represent table names in utility statements; there, the alias field is not used, and inh tells whether
 * to apply the operation recursively to child tables. In some contexts it is also useful to carry a TEMP table
 * indication here.
 * </p>
 * <p>
 * copied from: /postgresql-10rc1/src/include/nodes/primnodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class RangeVar extends Node {

    /** the catalog (database) name, or NULL */
    @XmlAttribute
    public String catalogname;

    /** the schema name, or NULL */
    @XmlAttribute
    public String schemaname;

    /** the relation/sequence name */
    @XmlAttribute
    public String relname;

    /**
     * expand rel by inheritance? recursively act on children?
     * 
     * @deprecated since 5.0 inhOpt is referred to as {@link #inh}
     */
    @XmlAttribute
    @Deprecated
    public InhOption inhOpt;

    /**
     * expand rel by inheritance? recursively act on children?
     * 
     * @since 5.0
     */
    @XmlAttribute
    public Boolean inh;

    @XmlAttribute
    public RelPersistence relpersistence;

    /** table alias &amp; optional column aliases */
    @XmlElement
    public Alias alias;

    /**
     * Constructor
     */
    public RangeVar() {
        type = NodeTag.T_RangeVar;
    }

    /**
     * Copy constructor
     *
     * @param toCopy
     *            The RangeVar to copy
     */
    public RangeVar(RangeVar toCopy) {
        super(toCopy);
        this.catalogname = toCopy.catalogname;
        this.schemaname = toCopy.schemaname;
        this.relname = toCopy.relname;
        this.inh = toCopy.inh;
        this.relpersistence = toCopy.relpersistence;
        if (toCopy.alias != null) {
            this.alias = toCopy.alias.clone();
        }

        if (toCopy.inhOpt != null) {
            throw new IllegalArgumentException(AlterExtensionContentsStmt.class.getName()
                    + ".inhOpt is not supported any more since version 5.0 (Postgres version 10)), contains: "
                    + toCopy.inhOpt);
        }
    }

    @Override
    public String toString() {
        if (inhOpt != null) {
            throw new AssertionError("inhOpt has been replaced by inh in release 5.0: " + inh);
        }
        StringBuilder result = new StringBuilder();

        if (Boolean.FALSE.equals(inh)) {
            result.append("only ");
        }

        String separator = "";
        if (catalogname != null) {
            result.append(ParserUtil.identifierToSql(catalogname));
            separator = ".";
        }
        if (schemaname != null) {
            result.append(separator).append(ParserUtil.identifierToSql(schemaname));
            separator = ".";
        }
        if (relname != null) {
            result.append(separator).append(ParserUtil.identifierToSql(relname));
        }

        if (alias != null) {
            result.append(" as ").append(alias);
        }
        return result.toString();
    }

    @Override
    public RangeVar clone() {
        RangeVar clone = (RangeVar) super.clone();
        if (alias != null) {
            clone.alias = alias.clone();
        }
        return clone;
    }
}
