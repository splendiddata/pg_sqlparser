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

revoke all on my_table from public;
revoke grant option for select, insert, update, delete, trucnate, references, trigger on table schema1.table1, schema2.table2, table3 from role_1, role_2 ;
revoke select, insert, update, delete on all tables in schema schema1, schema2 from role;

revoke grant option for  select (col1, col2), update (col2, col3), references (col4) on table my_schema.my_table from role_x;
revoke all privileges (col6, col7) on my_table from public;

revoke grant option for usage, select on sequence my_sequence, my_schema.another_sequence from group group_1, group_2;
revoke update on all sequences in schema schema from group "group";
revoke all on all sequences in schema schema from my_group;

revoke grant option for create, connect, temporary, temp on database database from public;
revoke all on database database from me, you;

revoke grant option for usage on domain schema.dom1, dom2, dom3 from grp1, grp2, grp3;
revoke all privileges on domain dom1 from public;

revoke usage on foreign data wrapper fdw_name, another_fdw_name from grp, grp2;
revoke all on foreign data wrapper my_fdw from public;
revoke grant option for all privileges on foreign data wrapper my_fdw from my_group;

revoke grant option for usage on foreign server my_server, your_server from group my_group;
revoke all on foreign server my_server, your_server from public;

revoke execute on function my_function() from public;
revoke grant option for execute on function my_schema.my_function(in some_arg text, inout anoter_arg my_schema.my_type, in float, date, the_time time) from me, you, "someone else";
revoke all on all functions in schema schema_1, schema_2 from my_role;

revoke all privileges on language plpgsql from public;
revoke grant option for usage on language lang_1, lang_2 from me, you;

revoke select, update on large object 123, 456 from public;
revoke all on large object 789 from me, you;

revoke create, usage on schema schema_1, schema2 from public;
revoke grant option for all privileges on schema schema_1, schema_2 from me, you;

revoke all on tablespace tablespace_1 from public;
revoke grant option for create on tablespace ts1, ts2, ts3 from group me, you;

revoke usage on type schema_1.type1, type2, schema_2.type_2, "my schema"." my type" from public;
revoke grant option for all on type my.type from me, you;

revoke role_1, role_2 from role_3, role_4;
revoke admin option for role_1, role_2 from role_3, role_4;
