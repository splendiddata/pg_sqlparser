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

alter sequence if exists sequence increment by 10 minvalue -10 maxvalue 500000 start with 60 cache 78 no cycle owned by schema.table.column;
alter sequence schema.seq increment by -1 no minvalue no maxvalue restart with 60 cycle owned by none;
alter sequence if exists schema.sequence owner to "another owner";
alter sequence sequence owner to "another owner";
alter sequence seq rename to new_name;
alter sequence if exists schema.sequence rename to my_sequence;
alter sequence if exists schema.sequence set schema schema;
alter sequence sequence set schema schema;