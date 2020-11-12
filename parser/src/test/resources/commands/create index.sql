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

create index name on schema.table(a);

create index if not exists name on schema.table(a);

create unique index concurrently "index name" 
  on schema.table
    ( a collate schema.collation opclass asc nulls last
    , b desc nulls first
    , schema.function(c)
    , (d + e)
    )
  with (fillfactor = 75, buffering = off, fastupdate = off)  
  tablespace tablespace 
  where a > b 
  and d not in (select x from schema.y);