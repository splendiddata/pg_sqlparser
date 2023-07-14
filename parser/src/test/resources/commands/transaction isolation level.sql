/*
 * Copyright (c) Splendid Data Product Development B.V. 2023
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

show transaction isolation level;
show transaction_isolation;

set transaction isolation level serializable;
set transaction isolation level repeatable Read;
set transaction isolation level read committed;
set transaction isolation level read uncommitted;
set transaction read write;
set transaction read only;
set transaction deferrable;
set transaction not deferrable;
set transaction isolation level repeatable read, read only, not deferrable;

set transaction snapshot 'some_snapshod_id';

SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED, DEFERRABLE;