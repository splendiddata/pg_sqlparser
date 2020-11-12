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

create foreign table schema.table() server "127.0.0.1";
create foreign table if not exists "table" () server "127.0.0.1" options(a 'x', b 'y''z');
create foreign table if not exists schema.table(a a.type options(a 'a') not null default '15'::a.type) server "127.0.0.1";
create foreign table if not exists schema.table (
  a a.type options(a 'a') not null default '15'::a.type,
  b interval hour to minute constraint "not null" not null
) server my_server
options (a 'a', b 'b');