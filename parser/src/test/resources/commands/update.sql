/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2025
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

update my_table set a = 1, b = 'two';
update only schema.table alias set a = 'a', b = default, c = sel.c from schema.another_table sel where sel.u = 12345  and sel.xxx = alias.yyy returning  a as x, b, *;
with abc (x, y) as (select 1, col_1 from schema_1.bbb) update my_table * set u = abc.x where current of my_cursor;
update the_table set (col_a, col_b) = (values (1, 2)) where col_c = 'some value';
update the_table 
    set (col_a, col_b) = (values (1, 2))
    ,   col_c = 'who knows what'
    ,   (col_d, col_e) = (select x from table_y) 
where col_c = 'some value';
update the_table 
    set (col_a) = ('a')
    ,   (col_b, col_c) = ('b', 'c')
    ,   (col_d, col_e) = (select x from table_y)
    ,   col_f = col_d
where col_c = 'some value';
UPDATE tbl SET a.b[5] = a.b[3];