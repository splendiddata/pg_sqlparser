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

alter extension extension update;
alter extension extension update to "2.1.1";
alter extension ext set schema schema;
alter extension ext add aggregate an.aggregate(in a text, in b text);
alter extension ext drop aggregate an.aggregate(in a text, in b text);
alter extension ext add cast (schema.type as c.d);
alter extension ext drop cast (schema.type as schema.x);
alter extension ext add collation "collation";
alter extension ext drop collation "collation";
alter extension ext add conversion conversion;
alter extension ext drop conversion conversion;
alter extension ext add domain "some".domain;
alter extension ext drop domain "some".domain;
alter extension ext add event trigger trigger;
alter extension ext drop event trigger trigger;
alter extension ext add foreign data wrapper wrapper;
alter extension ext drop foreign data wrapper wrapper;
alter extension ext add foreign table "foreign".table;
alter extension ext drop foreign table "table";
alter extension ext add function schema.function(in a text, inout b schema.type);
alter extension ext drop function function();
alter extension ext add materialized view view;
alter extension ext drop materialized view materialized.view;
alter extension ext add operator !=(text, schema.b);
alter extension ext drop operator >=(text, int);
alter extension ext add operator class operator.class using index_method;
alter extension ext drop operator class class using index_method;
alter extension ext add operator family operator.family using index_method;
alter extension ext drop operator family family using index_method;
alter extension extension add procedural language plpgsql;
alter extension extension drop language plpgsql;
alter extension extension add schema schema;
alter extension extension drop schema schema;
alter extension extension add sequence sequence;
alter extension extension drop sequence schema.sequence;
alter extension extension add server server;
alter extension extension drop server some_server;
alter extension extension add table schema.table;
alter extension extension drop table "table";
alter extension extension add text search configuration search.configuration;
alter extension extension drop text search configuration search.configuration;
alter extension extension add text search dictionary search.dictionary;
alter extension extension drop text search dictionary search.dictionary;
alter extension extension add text search parser search.parser;
alter extension extension drop text search parser parser;
alter extension extension add text search template template;
alter extension extension drop text search template search.template;
alter extension extension add type "some".type;
alter extension extension drop type type;
alter extension extension add view schema.view;
alter extension extension drop view view;