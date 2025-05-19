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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Ad-hoc node for AT_AlterConstraint
 * <p>
 * Copied from /postgresql-18beta2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 18
 */
@XmlRootElement(namespace = "parser")
public class ATAlterConstraint extends Node {

    /** Constraint name */
    @XmlAttribute
    public String conname;

    /** changing enforceability properties? */
    @XmlAttribute
    public boolean alterEnforceability;

    /** ENFORCED? */
    @XmlAttribute
    public boolean is_enforced;

    /** changing deferrability properties? */
    @XmlAttribute
    public boolean alterDeferrability;

    /** DEFERRABLE? */
    @XmlAttribute
    public boolean deferrable;

    /** INITIALLY DEFERRED? */
    @XmlAttribute
    public boolean initdeferred;

    /** changing inheritability properties */
    @XmlAttribute
    public boolean alterInheritability;

    @XmlAttribute
    public boolean noinherit;

    /**
     * Constructor
     */
    public ATAlterConstraint() {
        super(NodeTag.T_AlterTableCmd);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            the ATAlterConstraint to copy
     */
    public ATAlterConstraint(ATAlterConstraint toCopy) {
        super(toCopy);
        this.conname = toCopy.conname;
        this.alterEnforceability = toCopy.alterEnforceability;
        this.is_enforced = toCopy.is_enforced;
        this.alterDeferrability = toCopy.alterDeferrability;
        this.deferrable = toCopy.deferrable;
        this.initdeferred = toCopy.initdeferred;
        this.alterInheritability = toCopy.alterInheritability;
        this.noinherit = toCopy.noinherit;
    }

    @Override
    public ATAlterConstraint clone() {
        return (ATAlterConstraint) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("constraint ").append(conname);
        if (alterDeferrability) {
            if (!deferrable) {
                result.append(" not");
            }
            result.append(" deferrable");
            result.append(" initially");
            if (initdeferred) {
                result.append(" deferred");
            } else {
                result.append(" immediate");
            }
        }
        if (alterEnforceability) {
            result.append(' ');
            if (!is_enforced) {
                result.append("not ");
            }
            result.append("enforced");
        }
        if (alterInheritability) {
            if (noinherit) {
                result.append(" no");
            }
            result.append(" inherit");
        }
        return result.toString();
    }
}
