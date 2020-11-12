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

create schema if not exists schema;
create schema my_schema authorization user_name
  create table tbl(col text, col2 int not null default 12, col3 timestamp(15) default now() primary key)
  create view view as select * from tbl;
create schema if not exists authorization some_user;
create schema authorization some_user
  create view mv as select a,b,c from schema.d;