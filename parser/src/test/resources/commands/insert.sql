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

insert into schema.table values (1, '2', 3);
insert into schema.table values (1, '2', 3) on conflict do nothing;
insert into schema.table values (1, '2', 3) on conflict do update set a = 1, b = '2' where c= 3;
insert into schema.table values (1, '2', 3) on conflict do update set a = 1, b = (select 'something else' from some_other_table);
with x as (select a from b) insert into my_table values (1, a, 3);
insert into some_schema.some_table default values;
insert into tbl(a, b, c) select a, 'x', "Column" from schema1.table1;
insert into tbl(a, "B", c) select a, 'x', "Column" from schema1.table1;
insert into some_schema.some_table default values returning *;
insert into tbl(a, b, c) values ('x', a, b) returning (a, c, f);
insert into tbl(a, b, c) values ('x', a, b) returning a, b, c as x, y;
insert into tbl(a, b, c) values ('x', a, b) on conflict do nothing returning a, b, c as x, y ;
insert into schema.table values (1, '2', 3) on conflict do nothing;
insert into schema.table values (1, '2', 3) on conflict do update set a = 1, b = '2' where c= 3;
insert into schema.table values (1, '2', 3) 
    on conflict (some_foreign_key) do update
       set a = 1
       ,   b = (select b_content from some_other_table where b_content like '%' || b || '%');
insert into schema.table values (1, '2', 3) 
    on conflict on constraint some_constraint do update
       set a = 1, b = default;       
insert into schema.table values (1, '2', 3) 
    on conflict (a_column, another_column) where c >= 20 do update
       set a = 1, b = default;