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
import com.splendiddata.sqlparser.enums.AttributeIdentity;
import com.splendiddata.sqlparser.enums.ConstrType;
import com.splendiddata.sqlparser.enums.FkConstrMatch;
import com.splendiddata.sqlparser.enums.NodeTag;

/**
 * Copied from /postgresql-9.3.4/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 0.0.1
 */
@XmlRootElement(namespace = "parser")
public class Constraint extends Node {
    /* from Foreign key action codes in /postgresql-10rc1/src/include/nodes/parsenodes.h */
    /**
     * 'a'
     */
    public static final int FKCONSTR_ACTION_NOACTION = 'a';
    /**
     * 'r'
     */
    public static final int FKCONSTR_ACTION_RESTRICT = 'r';
    /**
     * 'c'
     */
    public static final int FKCONSTR_ACTION_CASCADE = 'c';
    /**
     * 'n'
     */
    public static final int FKCONSTR_ACTION_SETNULL = 'n';
    /**
     * 'd'
     */
    public static final int FKCONSTR_ACTION_SETDEFAULT = 'd';

    @XmlAttribute
    public ConstrType contype;

    /** Constraint name, or NULL if unnamed */
    @XmlAttribute
    public String conname;

    /** DEFERRABLE? */
    @XmlAttribute
    public boolean deferrable;

    /** INITIALLY DEFERRED? */
    @XmlAttribute
    public boolean initdeferred;

    /** is constraint non-inheritable? */
    @XmlAttribute
    public boolean is_no_inherit;

    /** expr, as untransformed parse tree */
    @XmlElement
    public Node raw_expr;

    /** expr, as nodeToString representation */
    @XmlAttribute
    public String cooked_expr;

    /** @since 5.0 */
    @XmlAttribute
    public AttributeIdentity generated_when;

    /** String nodes naming referenced column(s) */
    @XmlElementWrapper(name = "keys")
    @XmlElement(name = "key")
    public List<Value> keys;

    /**
     * String nodes naming referenced nonkey column(s)
     * 
     * @since 6.0 - Postgres version 11
     */
    @XmlElementWrapper(name = "including")
    @XmlElement(name = "including")
    public List<Value> including;

    /** list of (IndexElem, operator name) pairs */
    @XmlElementWrapper(name = "exclusions")
    @XmlElement(name = "exclusion")
    public List<List<Node>> exclusions;

    /** options from WITH clause */
    @XmlElementWrapper(name = "options")
    @XmlElement(name = "option")
    public List<DefElem> options;

    /** existing index to use; otherwise NULL */
    @XmlAttribute
    public String indexname;

    /** index tablespace; NULL for default */
    @XmlAttribute
    public String indexspace;

    /** index access method; NULL for default */
    @XmlAttribute
    public String access_method;

    /** partial index predicate */
    @XmlElement
    public Node where_clause;

    /** Primary key table */
    @XmlElement
    public RangeVar pktable;

    /** Attributes of foreign key */
    @XmlElementWrapper(name = "fk_attrs")
    @XmlElement(name = "fk_attr")
    public List<Value> fk_attrs;

    /** Corresponding attrs in PK table */
    @XmlElementWrapper(name = "pk_attrs")
    @XmlElement(name = "pk_attr")
    public List<Value> pk_attrs;

    /** FULL, PARTIAL, SIMPLE */
    @XmlAttribute
    public FkConstrMatch fk_matchtype;

    /**
     * ON UPDATE action
     * <p>
     * One of
     * {@link #FKCONSTR_ACTION_NOACTION},{@link #FKCONSTR_ACTION_RESTRICT},{@link #FKCONSTR_ACTION_CASCADE},{@link #FKCONSTR_ACTION_SETDEFAULT}
     * or {@link #FKCONSTR_ACTION_SETNULL}
     * </p>
     */
    @XmlAttribute
    public int fk_upd_action;

    /**
     * ON DELETE action
     * <p>
     * One of
     * {@link #FKCONSTR_ACTION_NOACTION},{@link #FKCONSTR_ACTION_RESTRICT},{@link #FKCONSTR_ACTION_CASCADE},{@link #FKCONSTR_ACTION_SETDEFAULT}
     * or {@link #FKCONSTR_ACTION_SETNULL}
     * </p>
     */
    @XmlAttribute
    public int fk_del_action;

    /** skip validation of existing rows? */
    @XmlAttribute
    public boolean skip_validation;

    /** mark the new constraint as valid? */
    @XmlAttribute
    public boolean initially_valid;

    /**
     * Constructor
     */
    public Constraint() {
        super(NodeTag.T_Constraint);
    }

    /**
     * Cope constructor
     *
     * @param original
     *            to copy
     */
    public Constraint(Constraint original) {
        super(original);
        this.contype = original.contype;
        this.conname = original.conname;
        this.deferrable = original.deferrable;
        this.initdeferred = original.initdeferred;
        this.is_no_inherit = original.is_no_inherit;
        if (original.raw_expr != null) {
            this.raw_expr = original.raw_expr.clone();
        }
        this.cooked_expr = original.cooked_expr;
        this.generated_when = original.generated_when;
        if (original.keys != null) {
            this.keys = original.keys.clone();
        }
        if (original.including != null) {
            this.including = original.including.clone();
        }
        if (original.exclusions != null) {
            this.exclusions = original.exclusions.clone();
        }
        if (original.options != null) {
            this.options = original.options.clone();
        }
        this.indexname = original.indexname;
        this.indexspace = original.indexspace;
        this.access_method = original.access_method;
        if (original.where_clause != null) {
            this.where_clause = original.where_clause.clone();
        }
        if (original.pktable != null) {
            this.pktable = original.pktable.clone();
        }
        if (original.fk_attrs != null) {
            this.fk_attrs = original.fk_attrs.clone();
        }
        if (original.pk_attrs != null) {
            this.pk_attrs = original.pk_attrs.clone();
        }
        this.fk_matchtype = original.fk_matchtype;
        this.fk_upd_action = original.fk_upd_action;
        this.fk_del_action = original.fk_del_action;
        this.skip_validation = original.skip_validation;
        this.initially_valid = original.initially_valid;
    }

    @Override
    public Constraint clone() {
        Constraint clone = (Constraint) super.clone();
        if (raw_expr != null) {
            clone.raw_expr = raw_expr.clone();
        }
        if (keys != null) {
            clone.keys = keys.clone();
        }
        if (including != null) {
            clone.including = including.clone();
        }
        if (exclusions != null) {
            clone.exclusions = exclusions.clone();
        }
        if (options != null) {
            clone.options = options.clone();
        }
        if (where_clause != null) {
            clone.where_clause = where_clause.clone();
        }
        if (pktable != null) {
            clone.pktable = pktable.clone();
        }
        if (fk_attrs != null) {
            clone.fk_attrs = fk_attrs.clone();
        }
        if (pk_attrs != null) {
            clone.pk_attrs = pk_attrs.clone();
        }
        return clone;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (conname != null) {
            result.append("constraint ").append(ParserUtil.identifierToSql(conname)).append(' ');
        }

        switch (contype) {
        case CONSTR_ATTR_DEFERRABLE:
            result.append("deferrable");
            break;
        case CONSTR_ATTR_DEFERRED:
            result.append("initially deferred");
            break;
        case CONSTR_ATTR_IMMEDIATE:
            result.append("initially immediate");
            break;
        case CONSTR_ATTR_NOT_DEFERRABLE:
            result.append("not deferrable");
            break;
        case CONSTR_CHECK:
            result.append("check(").append(raw_expr).append(')');
            if (is_no_inherit) {
                result.append(" no inherit");
            }
            break;
        case CONSTR_DEFAULT:
            result.append("default ").append(raw_expr);
            break;
        case CONSTR_EXCLUSION:
            result.append("exclude");

            if (access_method != null) {
                result.append(" using ").append(access_method);
            }

            if (exclusions != null) {
                result.append(" (");
                String separator = "";
                for (List<Node> exclusion : exclusions) {
                    result.append(separator).append(exclusion.get(0)).append(" with ")
                            .append(ParserUtil.operatorNameToSql((List<Value>) exclusion.get(1)));
                    separator = ", ";
                }
                result.append(')');
            }
            break;
        case CONSTR_FOREIGN:

            if (fk_attrs != null) {
                result.append("foreign key");

                if (fk_attrs != null) {
                    result.append(" (");
                    String sep = "";
                    for (Value key : fk_attrs) {
                        result.append(sep).append(ParserUtil.identifierToSql(key.toString()));
                        sep = ", ";
                    }
                    result.append(')');
                }
            }

            if (pktable != null) {
                result.append("references");
                char separator = ' ';
                if (pktable.schemaname != null) {
                    result.append(separator).append(pktable.schemaname);
                    separator = '.';
                }

                result.append(separator).append(pktable.relname);

                if (pk_attrs != null) {
                    result.append(" (");
                    String sep = "";
                    for (Value key : pk_attrs) {
                        result.append(sep).append(ParserUtil.identifierToSql(key.toString()));
                        sep = ", ";
                    }
                    result.append(')');
                }
            }
            if (fk_matchtype != null) {
                switch (fk_matchtype) {
                case FKCONSTR_MATCH_FULL:
                    result.append(" match full");
                    break;
                case FKCONSTR_MATCH_PARTIAL:
                    result.append(" match partial");
                    break;
                default:
                    break;
                }
            }
            switch (fk_del_action) {
            case FKCONSTR_ACTION_RESTRICT:
                result.append(" on delete restrict");
                break;
            case FKCONSTR_ACTION_CASCADE:
                result.append(" on delete cascade");
                break;
            case FKCONSTR_ACTION_SETNULL:
                result.append(" on delete set null");
                break;
            case FKCONSTR_ACTION_SETDEFAULT:
                result.append(" on delete set default");
                break;
            default:
                break;
            }
            switch (fk_upd_action) {
            case FKCONSTR_ACTION_RESTRICT:
                result.append(" on update restrict");
                break;
            case FKCONSTR_ACTION_CASCADE:
                result.append(" on update cascade");
                break;
            case FKCONSTR_ACTION_SETNULL:
                result.append(" on update set null");
                break;
            case FKCONSTR_ACTION_SETDEFAULT:
                result.append(" on update set default");
                break;
            default:
                break;
            }
            break;
        case CONSTR_NOTNULL:
            result.append("not null");
            break;
        case CONSTR_NULL:
            result.append("null");
            break;
        case CONSTR_PRIMARY:
            result.append("primary key");

            if (keys != null) {
                result.append(" (");
                String sep = "";
                for (Value key : keys) {
                    result.append(sep).append(ParserUtil.identifierToSql(key.toString()));
                    sep = ", ";
                }
                result.append(')');
            }

            if (indexname != null) {
                result.append(" using index ").append(ParserUtil.identifierToSql(indexname));
            }

            break;
        case CONSTR_UNIQUE:
            result.append("unique");

            if (keys != null) {
                result.append(" (");
                String sep = "";
                for (Value key : keys) {
                    result.append(sep).append(ParserUtil.identifierToSql(key.toString()));
                    sep = ", ";
                }
                result.append(')');
            }

            if (indexname != null) {
                result.append(" using index ").append(ParserUtil.identifierToSql(indexname));
            }

            break;
        case CONSTR_IDENTITY:
            result.append(" generated");
            switch (generated_when) {
            case ATTRIBUTE_IDENTITY_ALWAYS:
                result.append(" always");
                break;
            case ATTRIBUTE_IDENTITY_BY_DEFAULT:
                result.append(" by default");
                break;
            default:
                return ParserUtil.reportUnknownValue(AttributeIdentity.class.getName(), generated_when, getClass());
            }
            result.append(" as identity");
            if (options != null) {
                char separator = '(';
                for (DefElem option : options) {
                    switch (option.defname) {
                    case "cycle":
                        if (((Value) option.arg).val.ival == 0) {
                            result.append(separator).append("no");
                            separator = ' ';
                        }
                        result.append(separator).append(option.defname);
                        separator = ' ';
                        break;
                    case "minvalue":
                    case "maxvalue":
                        if (option.arg == null) {
                            result.append(separator).append(" no ").append(option.defname);
                            separator = ' ';
                        } else {
                            result.append(separator).append(option.defname).append(' ').append(option.arg);
                            separator = ' ';
                        }
                        break;
                    default:
                        result.append(separator).append(option.defname).append(' ').append(option.arg);
                        separator = ' ';
                    }
                }
                result.append(')');
            }
            return result.toString();
        case CONSTR_GENERATED:
            result.append("generated");
            switch (generated_when) {
            case ATTRIBUTE_IDENTITY_ALWAYS:
                result.append(" always");
                break;
            case ATTRIBUTE_IDENTITY_BY_DEFAULT:
                result.append(" by default");
                break;
            default:
                return ParserUtil.reportUnknownValue(AttributeIdentity.class.getName(), generated_when, getClass());
            }
            if (raw_expr != null) {
                result.append(" as (").append(raw_expr).append(") stored");
            }
            return result.toString();
        default:
           result.append(" ??? enum value ").append(contype.getClass().getName()).append('.').append(contype).append(" is unknown to ").append(this.getClass().getName()).append(".toString() ???");
           return result.toString();
        }

        if (options != null) {
            result.append(" with (");
            String separator = "";
            for (DefElem option : options) {
                result.append(separator).append(ParserUtil.identifierToSql(option.defname));
                separator = ", ";
                if (option.arg != null) {
                    result.append(" = ").append(ParserUtil.toSqlTextString(option.arg));
                }
            }
            result.append(')');
        }

        if (indexspace != null) {
            result.append(" using index tablespace ").append(indexspace);
        }

        if (where_clause != null) {
            result.append(" where (").append(where_clause).append(')');
        }

        if (deferrable) {
            result.append(" deferrable");
        }

        if (initdeferred) {
            result.append(" initially deferred");
        }

        if (skip_validation) {
            result.append(" not valid");
        }

        return result.toString();
    }
}