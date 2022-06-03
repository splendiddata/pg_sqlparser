/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2022
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

package com.splendiddata.sqlparser.enums;

/**
 * When a command can act on several kinds of objects with only one parse structure required, use these constants to
 * designate the object type. Note that commands typically don't support all the types.
 * <p>
 * Copied from /postgresql-9.5alpha2/src/include/nodes/parsenodes.h
 * </p>
 *
 * @author Splendid Data Product Development B.V.
 * @since 3.0.0
 */
public enum ObjectType {
    OBJECT_AGGREGATE("aggregate"),
    /** type's attribute, when distinct from column */
    OBJECT_ATTRIBUTE("attribute"),
    OBJECT_CAST("cast"),
    OBJECT_COLUMN("column"),
    OBJECT_CONSTRAINT("constraint"),
    OBJECT_COLLATION("collation"),
    OBJECT_CONVERSION("conversion"),
    OBJECT_DATABASE("database"),
    OBJECT_DOMAIN("domain"),
    /**
     * Domain constraint
     * <p>
     * toString() will return an empty string because in com.splendiddata.sqlparser.structure.RenameStmt#toString() it
     * should return "domain" and in com.splendiddata.sqlparser.structure.CommentStmt#toString() it should return
     * "constraint". Wit the empty string return, classes can decide for themselves what to put.
     * </p>
     */
    OBJECT_DOMCONSTRAINT(""),
    OBJECT_EVENT_TRIGGER("event trigger"),
    OBJECT_EXTENSION("extension"),
    OBJECT_FDW("foreign data wrapper"),
    OBJECT_FOREIGN_SERVER("server"),
    OBJECT_FOREIGN_TABLE("foreign table"),
    OBJECT_FUNCTION("function"),
    OBJECT_INDEX("index"),
    OBJECT_LANGUAGE("language"),
    OBJECT_LARGEOBJECT("large object"),
    OBJECT_MATVIEW("materialized view"),
    OBJECT_OPCLASS("operator class"),
    OBJECT_OPERATOR("operator"),
    OBJECT_OPFAMILY("operator family"),
    /** @since Postgres 15 */
    OBJECT_PARAMETER_ACL("????? OBJECT_PARAMETER_ACL in " + ObjectType.class.getName() + " ?????"),
    OBJECT_POLICY("policy"),
    /** @since 6.0 - Postgres version 11 */
    OBJECT_PROCEDURE("procedure"),
    /** @since 5.0 */
    OBJECT_PUBLICATION("publication"),
    OBJECT_ROLE("role"),
    /** @since 6.0 - Postgres version 11 */
    OBJECT_ROUTINE("routine"),
    OBJECT_RULE("rule"),
    OBJECT_SCHEMA("schema"),
    OBJECT_SEQUENCE("sequence"),
    /** @since 5.0 */
    OBJECT_SUBSCRIPTION("subscription"),
    /** @since 5.0 */
    OBJECT_STATISTIC_EXT("statistics"),
    OBJECT_TABCONSTRAINT("constraint"),
    OBJECT_TABLE("table"),
    OBJECT_TABLESPACE("tablespace"),
    OBJECT_TRANSFORM("transform"),
    OBJECT_TRIGGER("trigger"),
    OBJECT_TSCONFIGURATION("text search configuration"),
    OBJECT_TSDICTIONARY("text search dictionary"),
    OBJECT_TSPARSER("text search parser"),
    OBJECT_TSTEMPLATE("text search template"),
    OBJECT_TYPE("type"),
    OBJECT_VIEW("view"),
    OBJECT_ACCESS_METHOD("access method");

    /**
     * String containing all values with "|" characters between them, that can be used as argument in a regular
     * expression.
     */
    public static final String REPLACEMENT_REGEXP_PART;

    private final String toString;

    private ObjectType(String str) {
        toString = str;
    }

    @Override
    public String toString() {
        return toString;
    }

    static {
        StringBuilder format = new StringBuilder();
        String separator = "";
        for (ObjectType type : values()) {
            format.append(separator).append(type.name());
            separator = "|";
        }
        REPLACEMENT_REGEXP_PART = format.toString();
    }
}
