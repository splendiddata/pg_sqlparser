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

create sequence sequence;
create sequence if not exists my_schema.my_sequence;
create temporary sequence sequence increment by 2 minvalue 55 maxvalue 987654 start with 78 cache 63 no cycle owned by schema.table.column;
create temp sequence my_sequence increment -12 no minvalue no maxvalue start -789 cycle owned by none;
