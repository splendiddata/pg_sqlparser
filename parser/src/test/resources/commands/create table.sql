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

create table "table" ("column" text);


create global temp table if not exists schema.table
  (  col_1 schma.type collate coll primary key check (value >= 'a' and value <= z)
  ,  like schema.source including defaults including constraints including comments
  )
  inherits (schema.parent1, schema.parent2)
  with (a = 'a', b, c = 44)
  on commit preserve rows
  tablespace tablespace;
  
create unlogged table  schema.table
  (  col_1 schema.type default 'x'::schema.type unique references schema.another_table match full on delete cascade on update cascade deferrable initially deferred
  ,  col_2 text not null constraint bladibla check (value like '%xyz%') no inherit references some_table(some_column) -- match partial
  ,  constraint constr_1 primary key (col_1, col_2) with (a = 'a', b) using index tablespace ts 
  ,  exclude using my_index_method ((col_1 || col_2) with schema.==, col3 with &) with (a = 'b', c) where (col_2 > 'abc')
  );

create global temp table if not exists schema.table
  (  col_1 schma.type collate coll primary key check (value >= 'a' and value <= z)
  ,  like schema.source including defaults including constraints including comments
  ,  constraint fk foreign key (col_a, col_b) references another.table (x, y) on delete set null
  )
  inherits (schema.parent1, schema.parent2)
  on commit drop
  tablespace tablespace;