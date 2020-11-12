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

select * from schema.table;
select * from tbl_a for no key update of tbl_b, tbl_c skip locked;
select a, b, count(c) from tbl_a group by grouping sets (a, b);
select a, b, count(c) from tbl_a group by cube(  (a < b), rollup (c < d, e) );
select array(select a, b);
select a < any (select b from c) from (select x from y) tbl;
select a <> all (select b from c) from d;
select a, (select b from c) as d from e natural left join f order by 2 using > nulls last,a desc nulls first;
select a operator(my_schema.<<>>) b from some_table;
select operator(my_schema.=?=) 'value';
select 15operator("my schema".=?=);
select * from some_schema.some_table order by 1 using operator(some_schema.<^<<), tbl_a.col_a using >> nulls last;
select 'i'::"char";
select * from rows from (f1('arg1') as (a text, b integer), f2(x, y) as (c varchar(10))) with ordinality as f(x,y,z);
SELECT x FROM test3cs WHERE x SIMILAR TO 'a%' escape '|';