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

alter table tbl_a enable row level security;
alter table my_schem.my_table disable row level security;
alter table tbl_a set logged;
alter table tbl_a set unlogged;
alter table my_schema.my_table drop constraint if exists constraint_name;
alter table if exists my_schema.my_table* rename constraint constraint_name to new_constraint_name;
