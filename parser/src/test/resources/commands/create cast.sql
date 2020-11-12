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

create cast (schema.type as text) with function a.function(schema.type) as assignment;
create cast (int as text) with function function(int, boolean) as implicit;
create cast (varchar as a.type) without function as assignment;
create cast (varchar as a.type) without function as implicit;
create cast (source.type as target.type) with inout as assignment;
create cast (source.type as target.type) with inout as implicit;