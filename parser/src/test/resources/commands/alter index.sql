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

alter index if exists schema.index rename to "someting else";
alter index index rename to "someting else";
alter index if exists schema.index set tablespace "some tablespace";
alter index schema.index set tablespace tablespace;
alter index if exists schema."some index" set (fillfactor = 75, another_factor = whatever);
alter index schema."some index" set (fillfactor = 75);
alter index schema."some index" reset (another_factor);
alter index "some index" reset (another_factor, another_factor);
alter index all in tablespace ts_name set tablespace another_ts_name;
alter index all in tablespace ts_name owned by role1 set tablespace another_ts_name nowait;
alter index all in tablespace ts_name owned by role1, role2, role3 set tablespace another_ts_name;
alter index idxname depends on extension extname