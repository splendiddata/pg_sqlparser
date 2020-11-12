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
public class LockStmt extends Node {
    /*
     * The following codes are copied from /postgresql-9.4.1/src/include/storage/lock.h
     */
    // private static final int NoLock = 0;
    /**
     * SELECT
     */
    private static final int AccessShareLock = 1;
    /**
     * SELECT FOR UPDATE/FOR SHARE
     */
    private static final int RowShareLock = 2;
    /**
     * INSERT, UPDATE, DELETE
     */
    private static final int RowExclusiveLock = 3;
    /**
     * VACUUM (non-FULL),ANALYZE, CREATE INDEX CONCURRENTLY
     */
    private static final int ShareUpdateExclusiveLock = 4;
    /** CREATE INDEX (WITHOUT CONCURRENTLY) */
    private static final int ShareLock = 5;
    /**
     * like EXCLUSIVE MODE, but allows ROW SHARE
     */
    private static final int ShareRowExclusiveLock = 6;
    /**
     * blocks ROW SHARE/SELECT...FOR UPDATE
     */
    private static final int ExclusiveLock = 7;
    /**
     * ALTER TABLE, DROP TABLE, VACUUM FULL, and unqualified LOCK TABLE
     */
    private static final int AccessExclusiveLock = 8;

    /** relations to lock */
    @XmlElementWrapper(name = "relations")
    @XmlElement(name = "relation")
    public List<RangeVar> relations;

    /** lock mode */
    @XmlAttribute
    public int mode;

    /** no wait mode */
    @XmlAttribute
    public boolean nowait;

    /**
     * Constructor
     */
    public LockStmt() {
        super(NodeTag.T_LockStmt);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The LockStmt to copy
     */
    public LockStmt(LockStmt original) {
        super(original);
        if (original.relations != null) {
            this.relations = original.relations.clone();
        }
        this.mode = original.mode;
        this.nowait = original.nowait;
    }

    @Override
    public LockStmt clone() {
        LockStmt clone = (LockStmt) super.clone();
        if (relations != null) {
            clone.relations = relations.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("lock");

        String separator = " ";
        for (RangeVar relation : relations) {
            result.append(separator).append(relation);
            separator = ", ";
        }

        switch (mode) {
        case AccessShareLock:
            result.append(" in access share mode");
            break;
        case RowShareLock:
            result.append(" in row share mode");
            break;
        case RowExclusiveLock:
            result.append(" in row exclusive mode");
            break;
        case ShareUpdateExclusiveLock:
            result.append(" in share update exclusive mode");
            break;
        case ShareLock:
            result.append(" in share mode");
            break;
        case ShareRowExclusiveLock:
            result.append(" in share row exclusive mode");
            break;
        case ExclusiveLock:
            result.append(" in exclusive mode");
            break;
        case AccessExclusiveLock:
            break;
        default:
            result.append(ParserUtil.reportUnknownValue("mode", Integer.valueOf(mode), getClass()));
            break;
        }

        if (nowait) {
            result.append(" nowait");
        }

        return result.toString();
    }
}
