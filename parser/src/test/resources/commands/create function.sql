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

create function function() returns a.type cost 10;

create or replace function schema.name
(  in some_name text default  'default text'
,  variadic another_name schema.type
,  schema.type default 'abc'::schema.type
) returns table (col_1 numeric(10,2), col_2 schema.type)
language my_language
window immutable
not leakproof
called on null input
returns null on null input
security definer
cost 12
rows 34
set my_parameter to 'x'
set another_parameter = 'y'
set last_parameter from current
as 'some definition';

create function function() as 'object file', 'link symbol' immutable strict;

create function my_schema.my_function() as
$my_function$
begin
	/*
	 * A function with some /* nested */ comment
	 */
	 and a $$ line.
	 A $_0_$ line as well and a $_1_$ line.
	 And -- $_2_$ in comment.
end;
$my_function$;
