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

alter text search configuration schema.name add mapping for aap, noot, mies with dictionary, dict_2;
alter text search configuration configuration alter mapping for wim with schema.dict_1;
alter text search configuration configuration alter mapping replace old.dictionary with new.dictionary;
alter text search configuration configuration alter mapping for token1, token2 replace old.dictionary with new.dictionary;
alter text search configuration configuration drop mapping for token1;
alter text search configuration configuration drop mapping if exists for token1, token2;
alter text search configuration schema.configuration rename to anothername;
alter text search configuration schema.configuration owner to another_owner;
alter text search configuration schema.configuration set schema another_schema;