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

security label on table my_table is 'label';
security label for my_provider on table my_schema.my_table is 'my ''security'' label';
security label for "some provider" on column my_schema.my_table.my_column is 'column label';
security label on column my_table.my_column is 'column label';
security label for "some provider" on aggregate my_schema.my_aggregate(in field text, in my_schema.my_type, another_field my_type, int order by in order_field float) is 'column label';
security label on aggregate my_aggregate(text) is 'label';
security label on database database is 'label';
security label on domain schema.domain is 'label';
security label on event trigger event_trigger is 'trigger_label';
security label for my_provider on foreign table my_foreign_table is 'some label';
security label for my_provider on function function() is 'label';
security label on function my.function(in argument_a argument_schema.argument_type, int, inout argtype) is 'label';
security label on large object 123456 is 'lbl';
security label on materialized view my.view is 'lbl';
security label on language lang is 'lbl';
security label for prov on procedural language lang is 'some ''label''';
security label on role role is 'label';
security label for provider on schema schema is 'schema label';
security label for my_provider on sequence schema.sequence is '';
security label on tablespace ts is 'lbl';
security label on type type is 'type';
security label for "my provider" on type schema.type is 'type';
security label on view view is 'view';
security label for "my provider" on view schema.view is 'view';
