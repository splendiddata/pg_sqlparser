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

set transaction isolation level serializable, read write, not deferrable;
set transaction isolation level repeatable read, read only;
set transaction isolation level read committed, deferrable;
set transaction isolation level read uncommitted;
set transaction read write;
set transaction deferrable;
set transaction snapshot 'snapshot_id';
set session characteristics as transaction read only, isolation level repeatable read;