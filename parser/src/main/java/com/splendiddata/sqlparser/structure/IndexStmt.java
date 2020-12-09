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

import com.splendiddata.sqlparser.ParserUtil;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Create Index Statement
 * <p>
 * This represents creation of an index and/or an associated constraint. If isconstraint is true, we should create a
 * pg_constraint entry along with the index. But if indexOid isn't InvalidOid, we are not creating an index, just a
 * UNIQUE/PKEY constraint using an existing index. isconstraint must always be true in this case, and the fields
 * describing the index properties are empty.
 * </p>
 * <p>
 * Copied from /postgresql-12beta2/src/include/nodes/parsenodes.h
 * </p>
 * 
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class IndexStmt extends Node {

    /** name of new index, or NULL for default */
    @XmlAttribute
    public String idxname;

    /** relation to build index on */
    @XmlElement
    public RangeVar relation;

    /**
     * OID of relation to build index on
     * 
     * @since 6.0 - Postgres version 11
     */
    public Oid relationId;

    /** name of access method (eg. btree) */
    @XmlAttribute
    public String accessMethod;

    /** tablespace, or NULL for default */
    @XmlAttribute
    public String tableSpace;

    /** columns to index: a list of IndexElem */
    @XmlElementWrapper(name = "indexParams")
    @XmlElement(name = "indexParam")
    public List<IndexElem> indexParams;

    /**
     * additional columns to index: a list of IndexElem
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlElementWrapper(name = "indexIncludingParams")
    @XmlElement(name = "indexIncludingParam")
    public List<IndexElem> indexIncludingParams;

    /** WITH clause options: a list of DefElem */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** qualification (partial-index predicate) */
    @XmlElement
    public Node whereClause;

    /** exclusion operator names, or NIL if none */
    @XmlElementWrapper(name = "excludeOpNames")
    @XmlElement(name = "excludeOpName")
    public List<Node> excludeOpNames;

    /** comment to apply to index, or NULL */
    @XmlAttribute
    public String idxcomment;

    /** OID of an existing index, if any */
    @XmlElement
    public Oid indexOid;

    /** relfilenode of existing storage, if any */
    @XmlElement
    public Oid oldNode;

    /**
     * rd_createSubid of oldNode
     * 
     * @since 8.0 - Postgres version 13
     */
    public int oldCreateSubid;

    /**
     * rd_firstRelfilenodeSubid of oldNode
     * 
     * @since 8.0 - Postgres version 13
     */
    public int oldFirstRelfilenodeSubid;

    /** is index unique? */
    @XmlAttribute
    public boolean unique;

    /** is index a primary key? */
    @XmlAttribute
    public boolean primary;

    /** is it for a pkey/unique constraint? */
    @XmlAttribute
    public boolean isconstraint;

    /** is the constraint DEFERRABLE? */
    @XmlAttribute
    public boolean deferrable;

    /** is the constraint INITIALLY DEFERRED? */
    @XmlAttribute
    public boolean initdeferred;

    /** true when transformIndexStmt is finished */
    @XmlAttribute
    public boolean transformed;

    /** should this be a concurrent index build? */
    @XmlAttribute
    public boolean concurrent;

    /** just do nothing if index already exists? */
    @XmlAttribute
    public boolean if_not_exists;

    /*
     * reset default_tablespace prior to executing
     * 
     * @since 7.0 - Postgres 12
     */
    @XmlAttribute
    public boolean reset_default_tblspc;

    /**
     * Constructor
     */
    public IndexStmt() {
        super(NodeTag.T_IndexStmt);
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     *            the IndexStmt to copy
     */
    public IndexStmt(IndexStmt toCopy) {
        super(toCopy);

        this.idxname = toCopy.idxname;
        if (toCopy.relation != null) {
            this.relation = toCopy.relation.clone();
        }
        if (toCopy.relationId != null) {
            this.relationId = toCopy.relationId.clone();
        }
        this.accessMethod = toCopy.accessMethod;
        this.tableSpace = toCopy.tableSpace;
        if (toCopy.indexParams != null) {
            this.indexParams = toCopy.indexParams.clone();
        }
        if (toCopy.indexIncludingParams != null) {
            this.indexIncludingParams = toCopy.indexIncludingParams.clone();
        }
        if (toCopy.options != null) {
            this.options = toCopy.options.clone();
        }
        if (toCopy.whereClause != null) {
            this.whereClause = toCopy.whereClause.clone();
        }
        if (toCopy.excludeOpNames != null) {
            this.excludeOpNames = toCopy.excludeOpNames.clone();
        }
        this.idxcomment = toCopy.idxcomment;
        if (toCopy.indexOid != null) {
            this.indexOid = toCopy.indexOid.clone();
        }
        if (toCopy.oldNode != null) {
            this.oldNode = toCopy.oldNode.clone();
        }
        this.oldCreateSubid = toCopy.oldCreateSubid;
        this.oldFirstRelfilenodeSubid = toCopy.oldFirstRelfilenodeSubid;
        this.unique = toCopy.unique;
        this.primary = toCopy.primary;
        this.isconstraint = toCopy.isconstraint;
        this.deferrable = toCopy.deferrable;
        this.initdeferred = toCopy.initdeferred;
        this.transformed = toCopy.transformed;
        this.concurrent = toCopy.concurrent;
        this.if_not_exists = toCopy.if_not_exists;
        this.reset_default_tblspc = toCopy.reset_default_tblspc;
    }

    @Override
    public IndexStmt clone() {
        IndexStmt clone = (IndexStmt) super.clone();
        if (relation != null) {
            clone.relation = relation.clone();
        }
        if (indexParams != null) {
            clone.indexParams = indexParams.clone();
        }
        if (indexIncludingParams != null) {
            clone.indexIncludingParams = indexIncludingParams.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        if (whereClause != null) {
            clone.whereClause = whereClause.clone();
        }
        if (excludeOpNames != null) {
            clone.excludeOpNames = excludeOpNames.clone();
        }
        if (indexOid != null) {
            clone.indexOid = indexOid.clone();
        }
        if (oldNode != null) {
            clone.oldNode = oldNode.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("create ");

        if (primary) {
            result.append("primary key ");
        } else {
            if (unique) {
                result.append("unique ");
            }

            result.append("index ");
        }

        if (concurrent) {
            result.append("concurrently ");
        }

        if (if_not_exists) {
            result.append("if not exists ");
        }

        if (idxname != null) {
            result.append(ParserUtil.identifierToSql(idxname));
        }

        result.append(" on ").append(relation);

        if (accessMethod != null && !"btree".equals(accessMethod)) {
            result.append(" using ").append(ParserUtil.identifierToSql(accessMethod));
        }

        if (indexParams != null) {
            result.append(indexParams);
        }

        if (options != null) {
            result.append(" with (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(option.defname);
                separator = ", ";
                if (option.arg != null) {
                    result.append(" = ").append(option.arg);
                }
            }
            result.append(')');
        }

        if (tableSpace != null) {
            result.append(" tablespace ").append(ParserUtil.identifierToSql(tableSpace));
        }

        if (whereClause != null) {
            result.append(" where ").append(whereClause);
        }

        if (reset_default_tblspc) {
            result.append(" ??? What does reset_default_tblspc=true do ???");
        }

        return result.toString();
    }
}
