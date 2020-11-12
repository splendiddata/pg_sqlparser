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

alter function function() immutable called on null input stable volatile not leakproof;
alter function schema.function(in a text, out b int, inout c schema.type) external security definer external security invoker;
alter function schema.function() returns null on null input leakproof security invoker cost 5 rows 100000 set set = a set b to default set c from current reset reset reset all restrict;
alter function funct(a) rename to new_name;
alter function f() owner to new_owner;
alter function f() set schema schema;
alter function f() depends on extension name