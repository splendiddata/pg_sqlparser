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

grant all on my_table to public;
grant select, insert, update, delete, trucnate, references, trigger on table schema1.table1, schema2.table2, table3 to role_1, role_2 with grant option;
grant select, insert, update, delete on all tables in schema schema1, schema2 to role;

grant select (col1, col2), update (col2, col3), references (col4) on table my_schema.my_table to role_x with grant option;
grant all privileges (col6, col7) on my_table to public;

grant usage, select on sequence my_sequence, my_schema.another_sequence to group group_1, group_2 with grant option;
grant update on all sequences in schema schema to group "group";
grant all on all sequences in schema schema to my_group;

grant create, connect, temporary, temp on database database to public with grant option;
grant all on database database to me, you;

grant usage on domain schema.dom1, dom2, dom3 to grp1, grp2, grp3 with grant option;
grant all privileges on domain dom1 to public;

grant usage on foreign data wrapper fdw_name, another_fdw_name to grp, grp2;
grant all on foreign data wrapper my_fdw to public;
grant all privileges on foreign data wrapper my_fdw to my_group with grant option;

grant usage on foreign server my_server, your_server to group my_group with grant option;
grant all on foreign server my_server, your_server to public;

grant execute on function my_function() to public;
grant execute on function my_schema.my_function(in some_arg text, inout anoter_arg my_schema.my_type, in float, date, the_time time) to me, you, "someone else" with grant option;
grant all on all functions in schema schema_1, schema_2 to my_role;

grant all privileges on language plpgsql to public;
grant usage on language lang_1, lang_2 to me, you with grant option;

grant select, update on large object 123, 456 to public;
grant all on large object 789 to me, you;

grant create, usage on schema schema_1, schema2 to public;
grant all privileges on schema schema_1, schema_2 to me, you with grant option;

grant all on tablespace tablespace_1 to public;
grant create on tablespace ts1, ts2, ts3 to group me, you with grant option;

grant usage on type schema_1.type1, type2, schema_2.type_2, "my schema"." my type" to public;
grant all on type my.type to me, you with grant option;

grant role_1, role_2 to role_3, role_4;
grant role_1, role_2 to role_3, role_4 with admin option;
