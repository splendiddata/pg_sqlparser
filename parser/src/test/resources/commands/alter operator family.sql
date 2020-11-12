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

alter operator family whatever using bladibla add operator 5 ~(text, none) for search, operator 6 =(text, int) for order by x, operator 7 <>(none, int);
alter operator family schema.whatever using bladibla add function 10 function1(), function 20 (varchar) f2(text, int, varchar);
alter operator family schema.whatever using index_method drop operator 100 (text), function 100 (varchar, point), operator 110 (name.type, other.type);
alter operator family whatever using bladibla rename to something_else;
alter operator family schema.whatever using bladibla rename to something_else;
alter operator family whatever using bladibla owner to someone_else;
alter operator family schema.whatever using bladibla owner to someone_else;
alter operator family whatever using bladibla set schema schema;
alter operator family schema.whatever using bladibla set schema another_schema;
