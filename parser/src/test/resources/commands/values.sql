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

values ('a', 2 , case  when extract (day  from current_date) < 15 then 'first half' else 'second half' end);
values (1, 2, 3), (4, 5, 6) order by 1 asc, 2 desc, 3 using < nulls first limit 15 offset 10;
values (1, 2, 3), (4, 5, 6) order by 1 asc, 2 desc fetch first 30 rows only;