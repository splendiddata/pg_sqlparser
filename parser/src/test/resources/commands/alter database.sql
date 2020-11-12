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

alter database db connection limit 10;
alter database "Data Base" with connection limit 20;
alter database database rename to "to";
alter database database owner to another_owner;
alter database db set tablespace a_tablespace;
alter database db set some_paramenter to 'abcd';
alter database db set a_aparmeter = 123456;
alter database db set a_aparmeter to default;
alter database db set a_aparmeter = default;
alter database db set a_aparmeter from current;
alter database db reset a_aparmeter;
alter database db reset all;
