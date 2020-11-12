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

create operator class class for type type using index_method as storage storage;

create operator class schema.class
  default for type schema.type
  using index_method family schema.family
  as operator 15 *&^% (schema.type, schema.type) for search
  ,  operator 16 %!! (int, cstring) for order by schema.sort_family_name
  ,  function 17 schema.function(text, schema.type)
  ,  function 18 (type, schema.type) function_name()
  ,  storage schema.storage;