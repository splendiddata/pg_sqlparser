/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2024
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Initially copied from /postgresql-10.0/src/include/parser/kwlist.h
 * <p>
 * To update:
 * <ul>
 * <li>copy the PG_KEYWORD entries from src/include/parser/kwlist.h over the keywords.</li>
 * <li>Do a regular expression replace from
 * 
 * <pre>
 * PG_KEYWORD\(("[^"]+"),\s*(\w+),\s*(\w+),\s*\w+\)
 * </pre>
 * 
 * to
 * 
 * <pre>
 * $2($1, $3),
 * </pre>
 * 
 * </li>
 * <li>Replace the last comma in the list by a semi colon</li>
 * <li>Compare with the head version to re-introduce the &#64;since tags and add new &#64;since tags for the new keywords.
 * </ul>
 *
 * @author Splendid Data Product Development B.V.
 * @since Postgres 10
 */
public enum Keyword implements ScanKeywordCategory {
    ABORT_P("abort", UNRESERVED_KEYWORD),
    /** @since Postgres 16 */
    ABSENT("absent", UNRESERVED_KEYWORD),
    ABSOLUTE_P("absolute", UNRESERVED_KEYWORD),
    ACCESS("access", UNRESERVED_KEYWORD),
    ACTION("action", UNRESERVED_KEYWORD),
    ADD_P("add", UNRESERVED_KEYWORD),
    ADMIN("admin", UNRESERVED_KEYWORD),
    AFTER("after", UNRESERVED_KEYWORD),
    AGGREGATE("aggregate", UNRESERVED_KEYWORD),
    /** @since Postgres 11 */
    ALL("all", RESERVED_KEYWORD),
    ALSO("also", UNRESERVED_KEYWORD),
    ALTER("alter", UNRESERVED_KEYWORD),
    ALWAYS("always", UNRESERVED_KEYWORD),
    ANALYSE("analyse", RESERVED_KEYWORD), /* British spelling */
    ANALYZE("analyze", RESERVED_KEYWORD),
    AND("and", RESERVED_KEYWORD),
    ANY("any", RESERVED_KEYWORD),
    ARRAY("array", RESERVED_KEYWORD),
    AS("as", RESERVED_KEYWORD),
    ASC("asc", RESERVED_KEYWORD),
    /** @since Postgres 14 */
    ASENSITIVE("asensitive", UNRESERVED_KEYWORD),
    ASSERTION("assertion", UNRESERVED_KEYWORD),
    ASSIGNMENT("assignment", UNRESERVED_KEYWORD),
    ASYMMETRIC("asymmetric", RESERVED_KEYWORD),
    AT("at", UNRESERVED_KEYWORD),
    /** @since Postgres 14 */
    ATOMIC("atomic", UNRESERVED_KEYWORD),
    ATTACH("attach", UNRESERVED_KEYWORD),
    ATTRIBUTE("attribute", UNRESERVED_KEYWORD),
    AUTHORIZATION("authorization", TYPE_FUNC_NAME_KEYWORD),
    BACKWARD("backward", UNRESERVED_KEYWORD),
    BEFORE("before", UNRESERVED_KEYWORD),
    BEGIN_P("begin", UNRESERVED_KEYWORD),
    BETWEEN("between", COL_NAME_KEYWORD),
    BIGINT("bigint", COL_NAME_KEYWORD),
    BINARY("binary", TYPE_FUNC_NAME_KEYWORD),
    BIT("bit", COL_NAME_KEYWORD),
    BOOLEAN_P("boolean", COL_NAME_KEYWORD),
    BOTH("both", RESERVED_KEYWORD),
    /** @since Postgres 14 */
    BREADTH("breadth", UNRESERVED_KEYWORD),
    BY("by", UNRESERVED_KEYWORD),
    CACHE("cache", UNRESERVED_KEYWORD),
    CALL("call", UNRESERVED_KEYWORD),
    CALLED("called", UNRESERVED_KEYWORD),
    CASCADE("cascade", UNRESERVED_KEYWORD),
    CASCADED("cascaded", UNRESERVED_KEYWORD),
    CASE("case", RESERVED_KEYWORD),
    CAST("cast", RESERVED_KEYWORD),
    CATALOG_P("catalog", UNRESERVED_KEYWORD),
    CHAIN("chain", UNRESERVED_KEYWORD),
    CHAR_P("char", COL_NAME_KEYWORD),
    CHARACTER("character", COL_NAME_KEYWORD),
    CHARACTERISTICS("characteristics", UNRESERVED_KEYWORD),
    CHECK("check", RESERVED_KEYWORD),
    CHECKPOINT("checkpoint", UNRESERVED_KEYWORD),
    CLASS("class", UNRESERVED_KEYWORD),
    CLOSE("close", UNRESERVED_KEYWORD),
    CLUSTER("cluster", UNRESERVED_KEYWORD),
    COALESCE("coalesce", COL_NAME_KEYWORD),
    COLLATE("collate", RESERVED_KEYWORD),
    COLLATION("collation", TYPE_FUNC_NAME_KEYWORD),
    COLUMN("column", RESERVED_KEYWORD),
    COLUMNS("columns", UNRESERVED_KEYWORD),
    COMMENT("comment", UNRESERVED_KEYWORD),
    COMMENTS("comments", UNRESERVED_KEYWORD),
    COMMIT("commit", UNRESERVED_KEYWORD),
    COMMITTED("committed", UNRESERVED_KEYWORD),
    /** @since Postgres 14 */
    COMPRESSION("compression", UNRESERVED_KEYWORD),
    CONCURRENTLY("concurrently", TYPE_FUNC_NAME_KEYWORD),
    /** @since Postgres 17 */
    CONDITIONAL("conditional", UNRESERVED_KEYWORD),
    CONFIGURATION("configuration", UNRESERVED_KEYWORD),
    CONFLICT("conflict", UNRESERVED_KEYWORD),
    CONNECTION("connection", UNRESERVED_KEYWORD),
    CONSTRAINT("constraint", RESERVED_KEYWORD),
    CONSTRAINTS("constraints", UNRESERVED_KEYWORD),
    CONTENT_P("content", UNRESERVED_KEYWORD),
    CONTINUE_P("continue", UNRESERVED_KEYWORD),
    CONVERSION_P("conversion", UNRESERVED_KEYWORD),
    COPY("copy", UNRESERVED_KEYWORD),
    COST("cost", UNRESERVED_KEYWORD),
    CREATE("create", RESERVED_KEYWORD),
    CROSS("cross", TYPE_FUNC_NAME_KEYWORD),
    CSV("csv", UNRESERVED_KEYWORD),
    CUBE("cube", UNRESERVED_KEYWORD),
    CURRENT_P("current", UNRESERVED_KEYWORD),
    CURRENT_CATALOG("current_catalog", RESERVED_KEYWORD),
    CURRENT_DATE("current_date", RESERVED_KEYWORD),
    CURRENT_ROLE("current_role", RESERVED_KEYWORD),
    CURRENT_SCHEMA("current_schema", TYPE_FUNC_NAME_KEYWORD),
    CURRENT_TIME("current_time", RESERVED_KEYWORD),
    CURRENT_TIMESTAMP("current_timestamp", RESERVED_KEYWORD),
    CURRENT_USER("current_user", RESERVED_KEYWORD),
    CURSOR("cursor", UNRESERVED_KEYWORD),
    CYCLE("cycle", UNRESERVED_KEYWORD),
    DATA_P("data", UNRESERVED_KEYWORD),
    DATABASE("database", UNRESERVED_KEYWORD),
    DAY_P("day", UNRESERVED_KEYWORD),
    DEALLOCATE("deallocate", UNRESERVED_KEYWORD),
    DEC("dec", COL_NAME_KEYWORD),
    DECIMAL_P("decimal", COL_NAME_KEYWORD),
    DECLARE("declare", UNRESERVED_KEYWORD),
    DEFAULT("default", RESERVED_KEYWORD),
    DEFAULTS("defaults", UNRESERVED_KEYWORD),
    DEFERRABLE("deferrable", RESERVED_KEYWORD),
    DEFERRED("deferred", UNRESERVED_KEYWORD),
    DEFINER("definer", UNRESERVED_KEYWORD),
    DELETE_P("delete", UNRESERVED_KEYWORD),
    DELIMITER("delimiter", UNRESERVED_KEYWORD),
    DELIMITERS("delimiters", UNRESERVED_KEYWORD),
    DEPENDS("depends", UNRESERVED_KEYWORD),
    /** @since Postgres 14 */
    DEPTH("depth", UNRESERVED_KEYWORD),
    DESC("desc", RESERVED_KEYWORD),
    DETACH("detach", UNRESERVED_KEYWORD),
    DICTIONARY("dictionary", UNRESERVED_KEYWORD),
    DISABLE_P("disable", UNRESERVED_KEYWORD),
    DISCARD("discard", UNRESERVED_KEYWORD),
    DISTINCT("distinct", RESERVED_KEYWORD),
    DO("do", RESERVED_KEYWORD),
    DOCUMENT_P("document", UNRESERVED_KEYWORD),
    DOMAIN_P("domain", UNRESERVED_KEYWORD),
    DOUBLE_P("double", UNRESERVED_KEYWORD),
    DROP("drop", UNRESERVED_KEYWORD),
    EACH("each", UNRESERVED_KEYWORD),
    ELSE("else", RESERVED_KEYWORD),
    /** @since Postgres 17 */
    EMPTY_P("empty", UNRESERVED_KEYWORD),
    ENABLE_P("enable", UNRESERVED_KEYWORD),
    ENCODING("encoding", UNRESERVED_KEYWORD),
    ENCRYPTED("encrypted", UNRESERVED_KEYWORD),
    END_P("end", RESERVED_KEYWORD),
    ENUM_P("enum", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    ERROR_P("error", UNRESERVED_KEYWORD),
    ESCAPE("escape", UNRESERVED_KEYWORD),
    EVENT("event", UNRESERVED_KEYWORD),
    EXCEPT("except", RESERVED_KEYWORD),
    EXCLUDE("exclude", UNRESERVED_KEYWORD),
    EXCLUDING("excluding", UNRESERVED_KEYWORD),
    EXCLUSIVE("exclusive", UNRESERVED_KEYWORD),
    EXECUTE("execute", UNRESERVED_KEYWORD),
    EXISTS("exists", COL_NAME_KEYWORD),
    EXPLAIN("explain", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    EXPRESSION("expression", UNRESERVED_KEYWORD),
    EXTENSION("extension", UNRESERVED_KEYWORD),
    EXTERNAL("external", UNRESERVED_KEYWORD),
    EXTRACT("extract", COL_NAME_KEYWORD),
    FALSE_P("false", RESERVED_KEYWORD),
    FAMILY("family", UNRESERVED_KEYWORD),
    FETCH("fetch", RESERVED_KEYWORD),
    FILTER("filter", UNRESERVED_KEYWORD),
    /** @since Postgres 14 */
    FINALIZE("finalize", UNRESERVED_KEYWORD),
    FIRST_P("first", UNRESERVED_KEYWORD),
    FLOAT_P("float", COL_NAME_KEYWORD),
    FOLLOWING("following", UNRESERVED_KEYWORD),
    FOR("for", RESERVED_KEYWORD),
    FORCE("force", UNRESERVED_KEYWORD),
    FOREIGN("foreign", RESERVED_KEYWORD),
    /** @since Postgres 16 */
    FORMAT("format", UNRESERVED_KEYWORD),
    FORWARD("forward", UNRESERVED_KEYWORD),
    FREEZE("freeze", TYPE_FUNC_NAME_KEYWORD),
    FROM("from", RESERVED_KEYWORD),
    FULL("full", TYPE_FUNC_NAME_KEYWORD),
    FUNCTION("function", UNRESERVED_KEYWORD),
    FUNCTIONS("functions", UNRESERVED_KEYWORD),
    GENERATED("generated", UNRESERVED_KEYWORD),
    GLOBAL("global", UNRESERVED_KEYWORD),
    GRANT("grant", RESERVED_KEYWORD),
    GRANTED("granted", UNRESERVED_KEYWORD),
    GREATEST("greatest", COL_NAME_KEYWORD),
    GROUP_P("group", RESERVED_KEYWORD),
    GROUPING("grouping", COL_NAME_KEYWORD),
    /** @since Postgres 11 */
    GROUPS("groups", UNRESERVED_KEYWORD),
    HANDLER("handler", UNRESERVED_KEYWORD),
    HAVING("having", RESERVED_KEYWORD),
    HEADER_P("header", UNRESERVED_KEYWORD),
    HOLD("hold", UNRESERVED_KEYWORD),
    HOUR_P("hour", UNRESERVED_KEYWORD),
    IDENTITY_P("identity", UNRESERVED_KEYWORD),
    IF_P("if", UNRESERVED_KEYWORD),
    ILIKE("ilike", TYPE_FUNC_NAME_KEYWORD),
    IMMEDIATE("immediate", UNRESERVED_KEYWORD),
    IMMUTABLE("immutable", UNRESERVED_KEYWORD),
    IMPLICIT_P("implicit", UNRESERVED_KEYWORD),
    IMPORT_P("import", UNRESERVED_KEYWORD),
    IN_P("in", RESERVED_KEYWORD),
    /** @since Postgres 11 */
    INCLUDE("include", UNRESERVED_KEYWORD),
    INCLUDING("including", UNRESERVED_KEYWORD),
    INCREMENT("increment", UNRESERVED_KEYWORD),
    /** @since Postgres 16 */
    INDENT("indent", UNRESERVED_KEYWORD),
    INDEX("index", UNRESERVED_KEYWORD),
    INDEXES("indexes", UNRESERVED_KEYWORD),
    INHERIT("inherit", UNRESERVED_KEYWORD),
    INHERITS("inherits", UNRESERVED_KEYWORD),
    INITIALLY("initially", RESERVED_KEYWORD),
    INLINE_P("inline", UNRESERVED_KEYWORD),
    INNER_P("inner", TYPE_FUNC_NAME_KEYWORD),
    INOUT("inout", COL_NAME_KEYWORD),
    INPUT_P("input", UNRESERVED_KEYWORD),
    INSENSITIVE("insensitive", UNRESERVED_KEYWORD),
    INSERT("insert", UNRESERVED_KEYWORD),
    INSTEAD("instead", UNRESERVED_KEYWORD),
    INT_P("int", COL_NAME_KEYWORD),
    INTEGER("integer", COL_NAME_KEYWORD),
    INTERSECT("intersect", RESERVED_KEYWORD),
    INTERVAL("interval", COL_NAME_KEYWORD),
    INTO("into", RESERVED_KEYWORD),
    INVOKER("invoker", UNRESERVED_KEYWORD),
    IS("is", TYPE_FUNC_NAME_KEYWORD),
    ISNULL("isnull", TYPE_FUNC_NAME_KEYWORD),
    ISOLATION("isolation", UNRESERVED_KEYWORD),
    JOIN("join", TYPE_FUNC_NAME_KEYWORD),
    /** @since Postgres 16 */
    JSON("json", COL_NAME_KEYWORD),
    /** @since Postgres 16 */
    JSON_ARRAY("json_array", COL_NAME_KEYWORD),
    /** @since Postgres 16 */
    JSON_ARRAYAGG("json_arrayagg", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_EXISTS("json_exists", COL_NAME_KEYWORD),
    /** @since Postgres 16 */
    JSON_OBJECT("json_object", COL_NAME_KEYWORD),
    /** @since Postgres 16 */
    JSON_OBJECTAGG("json_objectagg", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_QUERY("json_query", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_SCALAR("json_scalar", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_SERIALIZE("json_serialize", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_TABLE("json_table", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    JSON_VALUE("json_value", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    KEEP("keep", UNRESERVED_KEYWORD),
    KEY("key", UNRESERVED_KEYWORD),
    /** @since Postgres 16 */
    KEYS("keys", UNRESERVED_KEYWORD),
    LABEL("label", UNRESERVED_KEYWORD),
    LANGUAGE("language", UNRESERVED_KEYWORD),
    LARGE_P("large", UNRESERVED_KEYWORD),
    LAST_P("last", UNRESERVED_KEYWORD),
    LATERAL_P("lateral", RESERVED_KEYWORD),
    LEADING("leading", RESERVED_KEYWORD),
    LEAKPROOF("leakproof", UNRESERVED_KEYWORD),
    LEAST("least", COL_NAME_KEYWORD),
    LEFT("left", TYPE_FUNC_NAME_KEYWORD),
    LEVEL("level", UNRESERVED_KEYWORD),
    LIKE("like", TYPE_FUNC_NAME_KEYWORD),
    LIMIT("limit", RESERVED_KEYWORD),
    LISTEN("listen", UNRESERVED_KEYWORD),
    LOAD("load", UNRESERVED_KEYWORD),
    LOCAL("local", UNRESERVED_KEYWORD),
    LOCALTIME("localtime", RESERVED_KEYWORD),
    LOCALTIMESTAMP("localtimestamp", RESERVED_KEYWORD),
    LOCATION("location", UNRESERVED_KEYWORD),
    LOCK_P("lock", UNRESERVED_KEYWORD),
    LOCKED("locked", UNRESERVED_KEYWORD),
    LOGGED("logged", UNRESERVED_KEYWORD),
    MAPPING("mapping", UNRESERVED_KEYWORD),
    MATCH("match", UNRESERVED_KEYWORD),
    /** @since Postgres 15 */
    MATCHED("matched", UNRESERVED_KEYWORD),
    MATERIALIZED("materialized", UNRESERVED_KEYWORD),
    MAXVALUE("maxvalue", UNRESERVED_KEYWORD),
    /** @since Postgres 15 */
    MERGE("merge", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    MERGE_ACTION("merge_action", COL_NAME_KEYWORD),
    METHOD("method", UNRESERVED_KEYWORD),
    MINUTE_P("minute", UNRESERVED_KEYWORD),
    MINVALUE("minvalue", UNRESERVED_KEYWORD),
    MODE("mode", UNRESERVED_KEYWORD),
    MONTH_P("month", UNRESERVED_KEYWORD),
    MOVE("move", UNRESERVED_KEYWORD),
    NAME_P("name", UNRESERVED_KEYWORD),
    NAMES("names", UNRESERVED_KEYWORD),
    NATIONAL("national", COL_NAME_KEYWORD),
    NATURAL("natural", TYPE_FUNC_NAME_KEYWORD),
    NCHAR("nchar", COL_NAME_KEYWORD),
    /** @since Postgres 17 */
    NESTED("nested", UNRESERVED_KEYWORD),
    NEW("new", UNRESERVED_KEYWORD),
    NEXT("next", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    NFC("nfc", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    NFD("nfd", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    NFKC("nfkc", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    NFKD("nfkd", UNRESERVED_KEYWORD),
    NO("no", UNRESERVED_KEYWORD),
    NONE("none", COL_NAME_KEYWORD),
    /** @since Postgres 13 */
    NORMALIZE("normalize", COL_NAME_KEYWORD),
    /** @since Postgres 13 */
    NORMALIZED("normalized", UNRESERVED_KEYWORD),
    NOT("not", RESERVED_KEYWORD),
    NOTHING("nothing", UNRESERVED_KEYWORD),
    NOTIFY("notify", UNRESERVED_KEYWORD),
    NOTNULL("notnull", TYPE_FUNC_NAME_KEYWORD),
    NOWAIT("nowait", UNRESERVED_KEYWORD),
    NULL_P("null", RESERVED_KEYWORD),
    NULLIF("nullif", COL_NAME_KEYWORD),
    NULLS_P("nulls", UNRESERVED_KEYWORD),
    NUMERIC("numeric", COL_NAME_KEYWORD),
    OBJECT_P("object", UNRESERVED_KEYWORD),
    OF("of", UNRESERVED_KEYWORD),
    OFF("off", UNRESERVED_KEYWORD),
    OFFSET("offset", RESERVED_KEYWORD),
    OIDS("oids", UNRESERVED_KEYWORD),
    OLD("old", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    OMIT("omit", UNRESERVED_KEYWORD),
    ON("on", RESERVED_KEYWORD),
    ONLY("only", RESERVED_KEYWORD),
    OPERATOR("operator", UNRESERVED_KEYWORD),
    OPTION("option", UNRESERVED_KEYWORD),
    OPTIONS("options", UNRESERVED_KEYWORD),
    OR("or", RESERVED_KEYWORD),
    /** @since Postgres 11 */
    ORDER("order", RESERVED_KEYWORD),
    ORDINALITY("ordinality", UNRESERVED_KEYWORD),
    OTHERS("others", UNRESERVED_KEYWORD),
    OUT_P("out", COL_NAME_KEYWORD),
    OUTER_P("outer", TYPE_FUNC_NAME_KEYWORD),
    OVER("over", UNRESERVED_KEYWORD),
    OVERLAPS("overlaps", TYPE_FUNC_NAME_KEYWORD),
    OVERLAY("overlay", COL_NAME_KEYWORD),
    OVERRIDING("overriding", UNRESERVED_KEYWORD),
    OWNED("owned", UNRESERVED_KEYWORD),
    OWNER("owner", UNRESERVED_KEYWORD),
    PARALLEL("parallel", UNRESERVED_KEYWORD),
    /** @since Postgres 15 */
    PARAMETER("parameter", UNRESERVED_KEYWORD),
    PARSER("parser", UNRESERVED_KEYWORD),
    PARTIAL("partial", UNRESERVED_KEYWORD),
    PARTITION("partition", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    PARTITIONS("partitions", UNRESERVED_KEYWORD),
    PASSING("passing", UNRESERVED_KEYWORD),
    PASSWORD("password", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    PATH("path", UNRESERVED_KEYWORD),
    PLACING("placing", RESERVED_KEYWORD),
    /** @since Postgres 17 */
    PLAN("plan", UNRESERVED_KEYWORD),
    PLANS("plans", UNRESERVED_KEYWORD),
    POLICY("policy", UNRESERVED_KEYWORD),
    POSITION("position", COL_NAME_KEYWORD),
    PRECEDING("preceding", UNRESERVED_KEYWORD),
    PRECISION("precision", COL_NAME_KEYWORD),
    PREPARE("prepare", UNRESERVED_KEYWORD),
    PREPARED("prepared", UNRESERVED_KEYWORD),
    PRESERVE("preserve", UNRESERVED_KEYWORD),
    PRIMARY("primary", RESERVED_KEYWORD),
    PRIOR("prior", UNRESERVED_KEYWORD),
    PRIVILEGES("privileges", UNRESERVED_KEYWORD),
    PROCEDURAL("procedural", UNRESERVED_KEYWORD),
    PROCEDURE("procedure", UNRESERVED_KEYWORD),
    /** @since Postgres 11 */
    PROCEDURES("procedures", UNRESERVED_KEYWORD),
    PROGRAM("program", UNRESERVED_KEYWORD),
    PUBLICATION("publication", UNRESERVED_KEYWORD),
    QUOTE("quote", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    QUOTES("quotes", UNRESERVED_KEYWORD),
    RANGE("range", UNRESERVED_KEYWORD),
    READ("read", UNRESERVED_KEYWORD),
    REAL("real", COL_NAME_KEYWORD),
    REASSIGN("reassign", UNRESERVED_KEYWORD),
    RECHECK("recheck", UNRESERVED_KEYWORD),
    RECURSIVE("recursive", UNRESERVED_KEYWORD),
    REF_P("ref", UNRESERVED_KEYWORD),
    REFERENCES("references", RESERVED_KEYWORD),
    REFERENCING("referencing", UNRESERVED_KEYWORD),
    REFRESH("refresh", UNRESERVED_KEYWORD),
    REINDEX("reindex", UNRESERVED_KEYWORD),
    RELATIVE_P("relative", UNRESERVED_KEYWORD),
    RELEASE("release", UNRESERVED_KEYWORD),
    RENAME("rename", UNRESERVED_KEYWORD),
    REPEATABLE("repeatable", UNRESERVED_KEYWORD),
    REPLACE("replace", UNRESERVED_KEYWORD),
    REPLICA("replica", UNRESERVED_KEYWORD),
    RESET("reset", UNRESERVED_KEYWORD),
    RESTART("restart", UNRESERVED_KEYWORD),
    RESTRICT("restrict", UNRESERVED_KEYWORD),
    /** @since Postgres 14 */
    RETURN("return", UNRESERVED_KEYWORD),
    RETURNING("returning", RESERVED_KEYWORD),
    RETURNS("returns", UNRESERVED_KEYWORD),
    REVOKE("revoke", UNRESERVED_KEYWORD),
    RIGHT("right", TYPE_FUNC_NAME_KEYWORD),
    ROLE("role", UNRESERVED_KEYWORD),
    ROLLBACK("rollback", UNRESERVED_KEYWORD),
    ROLLUP("rollup", UNRESERVED_KEYWORD),
    /** @since Postgres 11 */
    ROUTINE("routine", UNRESERVED_KEYWORD),
    /** @since Postgres 11 */
    ROUTINES("routines", UNRESERVED_KEYWORD),
    ROW("row", COL_NAME_KEYWORD),
    ROWS("rows", UNRESERVED_KEYWORD),
    RULE("rule", UNRESERVED_KEYWORD),
    SAVEPOINT("savepoint", UNRESERVED_KEYWORD),
    /** @since Postgres 16 */
    SCALAR("scalar", UNRESERVED_KEYWORD),
    SCHEMA("schema", UNRESERVED_KEYWORD),
    SCHEMAS("schemas", UNRESERVED_KEYWORD),
    SCROLL("scroll", UNRESERVED_KEYWORD),
    SEARCH("search", UNRESERVED_KEYWORD),
    SECOND_P("second", UNRESERVED_KEYWORD),
    SECURITY("security", UNRESERVED_KEYWORD),
    SELECT("select", RESERVED_KEYWORD),
    SEQUENCE("sequence", UNRESERVED_KEYWORD),
    SEQUENCES("sequences", UNRESERVED_KEYWORD),
    SERIALIZABLE("serializable", UNRESERVED_KEYWORD),
    SERVER("server", UNRESERVED_KEYWORD),
    SESSION("session", UNRESERVED_KEYWORD),
    SESSION_USER("session_user", RESERVED_KEYWORD),
    SET("set", UNRESERVED_KEYWORD),
    SETOF("setof", COL_NAME_KEYWORD),
    SETS("sets", UNRESERVED_KEYWORD),
    SHARE("share", UNRESERVED_KEYWORD),
    SHOW("show", UNRESERVED_KEYWORD),
    SIMILAR("similar", TYPE_FUNC_NAME_KEYWORD),
    SIMPLE("simple", UNRESERVED_KEYWORD),
    SKIP("skip", UNRESERVED_KEYWORD),
    SMALLINT("smallint", COL_NAME_KEYWORD),
    SNAPSHOT("snapshot", UNRESERVED_KEYWORD),
    SOME("some", RESERVED_KEYWORD),
    /** @since Postgres 17 */
    SOURCE("source", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    SPLIT("split", UNRESERVED_KEYWORD),
    SQL_P("sql", UNRESERVED_KEYWORD),
    STABLE("stable", UNRESERVED_KEYWORD),
    STANDALONE_P("standalone", UNRESERVED_KEYWORD),
    START("start", UNRESERVED_KEYWORD),
    STATEMENT("statement", UNRESERVED_KEYWORD),
    STATISTICS("statistics", UNRESERVED_KEYWORD),
    STDIN("stdin", UNRESERVED_KEYWORD),
    STDOUT("stdout", UNRESERVED_KEYWORD),
    STORAGE("storage", UNRESERVED_KEYWORD),
    /** @since Postgres 12 */
    STORED("stored", UNRESERVED_KEYWORD),
    STRICT_P("strict", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    STRING_P("string", UNRESERVED_KEYWORD),
    STRIP_P("strip", UNRESERVED_KEYWORD),
    SUBSCRIPTION("subscription", UNRESERVED_KEYWORD),
    SUBSTRING("substring", COL_NAME_KEYWORD),
    /** @since Postgres 12 */
    SUPPORT("support", UNRESERVED_KEYWORD),
    SYMMETRIC("symmetric", RESERVED_KEYWORD),
    SYSID("sysid", UNRESERVED_KEYWORD),
    SYSTEM_P("system", UNRESERVED_KEYWORD),
    /** @since Postgres 16 */
    SYSTEM_USER("system_user", RESERVED_KEYWORD),
    TABLE("table", RESERVED_KEYWORD),
    TABLES("tables", UNRESERVED_KEYWORD),
    TABLESAMPLE("tablesample", TYPE_FUNC_NAME_KEYWORD),
    TABLESPACE("tablespace", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    TARGET("target", UNRESERVED_KEYWORD),
    TEMP("temp", UNRESERVED_KEYWORD),
    TEMPLATE("template", UNRESERVED_KEYWORD),
    TEMPORARY("temporary", UNRESERVED_KEYWORD),
    TEXT_P("text", UNRESERVED_KEYWORD),
    THEN("then", RESERVED_KEYWORD),
    /** @since Postgres 11 */
    TIES("ties", UNRESERVED_KEYWORD),
    TIME("time", COL_NAME_KEYWORD),
    TIMESTAMP("timestamp", COL_NAME_KEYWORD),
    TO("to", RESERVED_KEYWORD),
    TRAILING("trailing", RESERVED_KEYWORD),
    TRANSACTION("transaction", UNRESERVED_KEYWORD),
    TRANSFORM("transform", UNRESERVED_KEYWORD),
    TREAT("treat", COL_NAME_KEYWORD),
    TRIGGER("trigger", UNRESERVED_KEYWORD),
    TRIM("trim", COL_NAME_KEYWORD),
    TRUE_P("true", RESERVED_KEYWORD),
    TRUNCATE("truncate", UNRESERVED_KEYWORD),
    TRUSTED("trusted", UNRESERVED_KEYWORD),
    TYPE_P("type", UNRESERVED_KEYWORD),
    TYPES_P("types", UNRESERVED_KEYWORD),
    /** @since Postgres 13 */
    UESCAPE("uescape", UNRESERVED_KEYWORD),
    UNBOUNDED("unbounded", UNRESERVED_KEYWORD),
    UNCOMMITTED("uncommitted", UNRESERVED_KEYWORD),
    /** @since Postgres 17 */
    UNCONDITIONAL("unconditional", UNRESERVED_KEYWORD),
    UNENCRYPTED("unencrypted", UNRESERVED_KEYWORD),
    UNION("union", RESERVED_KEYWORD),
    UNIQUE("unique", RESERVED_KEYWORD),
    UNKNOWN("unknown", UNRESERVED_KEYWORD),
    UNLISTEN("unlisten", UNRESERVED_KEYWORD),
    UNLOGGED("unlogged", UNRESERVED_KEYWORD),
    UNTIL("until", UNRESERVED_KEYWORD),
    UPDATE("update", UNRESERVED_KEYWORD),
    USER("user", RESERVED_KEYWORD),
    USING("using", RESERVED_KEYWORD),
    VACUUM("vacuum", UNRESERVED_KEYWORD),
    VALID("valid", UNRESERVED_KEYWORD),
    VALIDATE("validate", UNRESERVED_KEYWORD),
    VALIDATOR("validator", UNRESERVED_KEYWORD),
    VALUE_P("value", UNRESERVED_KEYWORD),
    VALUES("values", COL_NAME_KEYWORD),
    VARCHAR("varchar", COL_NAME_KEYWORD),
    VARIADIC("variadic", RESERVED_KEYWORD),
    VARYING("varying", UNRESERVED_KEYWORD),
    VERBOSE("verbose", TYPE_FUNC_NAME_KEYWORD),
    VERSION_P("version", UNRESERVED_KEYWORD),
    VIEW("view", UNRESERVED_KEYWORD),
    VIEWS("views", UNRESERVED_KEYWORD),
    VOLATILE("volatile", UNRESERVED_KEYWORD),
    WHEN("when", RESERVED_KEYWORD),
    WHERE("where", RESERVED_KEYWORD),
    WHITESPACE_P("whitespace", UNRESERVED_KEYWORD),
    WINDOW("window", RESERVED_KEYWORD),
    WITH("with", RESERVED_KEYWORD),
    WITHIN("within", UNRESERVED_KEYWORD),
    WITHOUT("without", UNRESERVED_KEYWORD),
    WORK("work", UNRESERVED_KEYWORD),
    WRAPPER("wrapper", UNRESERVED_KEYWORD),
    WRITE("write", UNRESERVED_KEYWORD),
    XML_P("xml", UNRESERVED_KEYWORD),
    XMLATTRIBUTES("xmlattributes", COL_NAME_KEYWORD),
    XMLCONCAT("xmlconcat", COL_NAME_KEYWORD),
    XMLELEMENT("xmlelement", COL_NAME_KEYWORD),
    XMLEXISTS("xmlexists", COL_NAME_KEYWORD),
    XMLFOREST("xmlforest", COL_NAME_KEYWORD),
    XMLNAMESPACES("xmlnamespaces", COL_NAME_KEYWORD),
    XMLPARSE("xmlparse", COL_NAME_KEYWORD),
    XMLPI("xmlpi", COL_NAME_KEYWORD),
    XMLROOT("xmlroot", COL_NAME_KEYWORD),
    XMLSERIALIZE("xmlserialize", COL_NAME_KEYWORD),
    XMLTABLE("xmltable", COL_NAME_KEYWORD),
    YEAR_P("year", UNRESERVED_KEYWORD),
    YES_P("yes", UNRESERVED_KEYWORD),
    ZONE("zone", UNRESERVED_KEYWORD);

    private static final Map<String, Keyword> NAME_TO_KEYWORD;

    public final String name;
    /** see codes above */
    public final int category;

    /**
     * Constructor
     *
     * @param name
     *            The actual keyword
     * @param category
     *            The keyword category
     */
    private Keyword(String name, int category) {
        this.name = name;
        this.category = category;
    }

    /**
     * Returns the Keyword with the specified case-insensitive name or null if not found.
     * <p>
     * Note that the name is the content of the name field, and NOT what enum.name() returns.
     * </p>
     *
     * @param name
     *            The keyword in String format
     * @return Keyword or null if not found.
     */
    public static Keyword fromName(String name) {
        return NAME_TO_KEYWORD.get(name.toLowerCase());
    }

    static {
        Map<String, Keyword> map = new HashMap<>(Keyword.values().length * 10 / 7);
        for (Keyword keyword : Keyword.values()) {
            map.put(keyword.name, keyword);
        }
        NAME_TO_KEYWORD = Collections.unmodifiableMap(map);
    }
}

/**
 * Keyword categories from /postgresql-9.3.4/src/include/parser/keywords.h
 */
interface ScanKeywordCategory {
    int UNRESERVED_KEYWORD = 0;
    int COL_NAME_KEYWORD = 1;
    int TYPE_FUNC_NAME_KEYWORD = 2;
    int RESERVED_KEYWORD = 3;
}
