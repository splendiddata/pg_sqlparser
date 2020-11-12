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

comment on aggregate aggregate(in arg1 text, in arg2 text, arg3 int, in float, varchar order by in arg3 text, in arg4 text,  varchar, int) is 'text';
comment on aggregate an.aggregate(in arg1 text, in arg2 schema.type, arg3 int, in float, varchar order by in arg3 text, in arg4 text, in varchar, int) is 'another ''text''';
comment on cast (a.b as c.d) is null;
comment on collation a.b is 'text';
comment on column schema.table.column is '';
comment on constraint "constraint" on "schema"."table" is 'comment';
comment on conversion conversion.name is 'comment';
comment on database database is 'database';
comment on domain schema.domain is '''dom''ain';
comment on extension "ex tension" is null;
comment on event trigger eventtrigger is 'trg';
comment on foreign data wrapper wrapper is 'wrapper';
comment on foreign table schema.table is 'foreign';
comment on function schema.function(in name text, inout schema.type, varchar) is 'null';
comment on function function() is null;
comment on index schema.table.index is 'index';
comment on large object 123456 is 'lob';
comment on materialized view materialized.view is 'materialized view';
comment on operator = (text, a.b) is 'operator';
comment on operator ~ (none, a.b) is 'operator';
comment on operator @@ (a.b, none) is '@@ operator';
comment on operator class schema.class using method is '';
comment on operator family schema.family using method is 'family';
comment on procedural language plpgsql is 'who wants this?';
comment on language C is 'that''s ancient';
comment on role my_role is 'mine';
comment on rule rule on schema.table is 'a rule';
comment on schema schema is 'schema';
comment on sequence schema.sequence is 'sequence';
comment on server server is 'server';
comment on table schema.table is 'table comment';
comment on tablespace tablespace is 'tablespace';
comment on text search configuration schema.configuration is 'tsc';
comment on text search dictionary schema.dictoionary is 'tsd';
comment on text search parser schema.parser is 'tsp';
comment on text search template schema.template is 'tst';
comment on trigger trigger on tbl is 'trigger';
comment on type schema.type is 'type';
comment on view schema.view is 'view';
comment on constraint my_constraint on domain my_schema.my_domain is 'my comment';