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

alter materialized view if exists schema.viewname rename to "someting else";
alter materialized view view rename to "someting else";
alter materialized view if exists schema.view rename column "column" to new_column;
alter materialized view schema.index rename new_column to "column";
alter materialized view if exists schema."some view" set schema schema;
alter materialized view all in tablespace ts_name set tablespace another_ts_name;
alter materialized view all in tablespace ts_name owned by role1 set tablespace another_ts_name nowait;
alter materialized view all in tablespace ts_name owned by role1, role2, role3 set tablespace another_ts_name;
alter materialized view if exists schema.view alter column col set statistics 5;
alter materialized view schema.view alter "column" set statistics 5;
alter materialized view view alter column "column" set (a = a, b = b);
alter materialized view if exists view alter col set (a = a);
alter materialized view vw alter col reset (a);
alter materialized view vw alter col reset (a, b);
alter materialized view v alter column c set storage plain;
alter materialized view if exists v alter c set storage external;
alter materialized view v alter c set storage extended;
alter materialized view v alter c set storage main;
alter materialized view v cluster on index;
alter materialized view v set (a = a, b = b);
alter materialized view if exists v set (a = a);
alter materialized view if exists v reset (a, b, c);
alter materialized view if exists v reset (a);
alter materialized view view owner to "someone else";
alter materialized view view set tablespace tablespace;
alter materialized view test_view depends on extension extname