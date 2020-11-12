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

create view shema.view as select a, b, 10 from schema.table with local check option;
create or replace temporary recursive view viewname(col1, col2) with (a=a, b, c=23) as select 'a', b, "C" from a_table where x < y;
create or replace temp view viewname as select 'a', b, "C" from a_table where x < y with cascaded check option;
create or replace recursive view viewname(col1, col2)  as select 'a', b, "C" from my_schema.a_table where x < y;
