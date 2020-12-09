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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.splendiddata.sqlparser.enums.LockClauseStrength;
import com.splendiddata.sqlparser.enums.LockWaitPolicy;

/**
 * LockingClause - raw representation of FOR [NO KEY] UPDATE/[KEY] SHARE options
 * <p>
 * Note: lockedRels == NIL means "all relations in query". Otherwise it is a list of RangeVar nodes. (We use RangeVar
 * mainly because it carries a location field --- currently, parse analysis insists on unqualified names in
 * LockingClause.)
 * </p>
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
@XmlRootElement(namespace = "parser")
public class LockingClause extends Node {

    /** FOR [KEY] UPDATE/SHARE relations */
    @XmlElementWrapper(name = "lockedRels")
    @XmlElement(name = "lockedRel")
    public List<RangeVar> lockedRels;

    @XmlAttribute
    public LockClauseStrength strength;

    /** NOWAIT and SKIP LOCKED */
    @XmlAttribute
    public LockWaitPolicy waitPolicy;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("for ");
        switch (strength) {
        case LCS_FORKEYSHARE:
            result.append("key share");
            break;
        case LCS_FORNOKEYUPDATE:
            result.append("no key update");
            break;
        case LCS_FORSHARE:
            result.append("share");
            break;
        case LCS_FORUPDATE:
            result.append("update");
            break;
        default:
            throw new AssertionError("Unknowns LockClauseStrength: " + strength);
        }

        if (lockedRels != null) {
            String separator = " of ";
            for (RangeVar lockedRel : lockedRels) {
                result.append(separator).append(lockedRel);
                separator = ", ";
            }
        }

        if (waitPolicy != null) {
            result.append(waitPolicy);
        }

        return result.toString();
    }

    @Override
    public LockingClause clone() {
        LockingClause clone = (LockingClause) super.clone();
        if (lockedRels != null) {
            clone.lockedRels = lockedRels.clone();
        }
        return clone;
    }
}
