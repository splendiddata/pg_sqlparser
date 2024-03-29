/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2023
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

alter tablespace a rename to b;
alter tablespace tablespace owner to owner;
alter tablespace tablespace set (a = a);
alter tablespace tablespace set (a = a, b = b);
alter tablespace ts reset (a);
alter tablespace ts reset (a, "table", b);

drop tablespace if exists ts_to_be_dropped;