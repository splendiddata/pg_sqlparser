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

vacuum;
vacuum (full, freeze, analyze, verbose, disable_page_skipping) schema.table(column1, "column 2");
vacuum (full, verbose, skip_locked, index_cleanup false) schema.table;
vacuum (analyze, full, verbose) schema.table;
vacuum (analyze, verbose) schema.table(column1, "column 2");
vacuum (freeze) schema.table(column1, "column 2");
vacuum full freeze verbose my_schema.my_table;
vacuum verbose my_schema.my_table;
vacuum full freeze my_schema.my_table;
vacuum full freeze verbose analyze schema.table(column1, "column 2");
vacuum analyze schema.table(column1, "column 2");
vacuum freeze analyze schema.table(column1, "column 2");