/*
 * Json operators from the manual
 */
select '[{"a":"foo"},{"b":"bar"},{"c":"baz"}]'::json -> 2;
select '{"a": {"b":"foo"}}'::json -> 'a';
select '[1,2,3]'::json ->> 2;
select '{"a": {"b": ["foo","bar"]}}'::json #> '{a,b,1}';
select '{"a": {"b": ["foo","bar"]}}'::json #>> '{a,b,1}';
select '{"a":1, "b":2}'::jsonb @> '{"b":2}'::jsonb;
select '{"b":2}'::jsonb <@ '{"a":1, "b":2}'::jsonb;
select '{"a":1, "b":2}'::jsonb ? 'b';
select '["a", "b", "c"]'::jsonb ? 'b';
select '["a", "b"]'::jsonb || '["a", "d"]'::jsonb;
select '{"a": "b"}'::jsonb || '{"c": "d"}'::jsonb;
select '[1, 2]'::jsonb || '3'::jsonb;
select '{"a": "b"}'::jsonb || '42'::jsonb;
select '[1, 2]'::jsonb || jsonb_build_array('[3, 4]'::jsonb);
select '{"a": "b", "c": "d"}'::jsonb - 'a';
select '{"a": "b", "c": "d"}'::jsonb - '{a,c}'::text[];
select '["a", "b"]'::jsonb - 1;
select '["a", {"b":1}]'::jsonb #- '{1,b}';
select '{"a":[1,2,3,4,5]}'::jsonb @? '$.a[*] ? (@ > 2)';
select '{"a":[1,2,3,4,5]}'::jsonb @@ '$.a[*]';
/*
 * functions from the Postgres manual
 */
select to_json('Fred said "Hi."'::text);
select to_jsonb(row(42, 'Fred said "Hi."'::text));
select array_to_json('{{1,5},{99,100}}'::int[]);
select json_array(1,true,json '{"a":null}');
select json_array(1,true,json '{"a":null}' null on null);
select json_array(1,true,json '{"a":null}' returning a_type);
select json_array(SELECT * FROM (VALUES(1),(2)) t);
select row_to_json(row(1,'foo'));
select json_build_array(1, 2, 'foo', 4, 5);
select json_build_object('foo', 1, 2, row(3,'bar'));
select json_object('code' VALUE 'P123', 'title': 'Jaws');
select json_object('code' VALUE 'P123', 'title': 'Jaws' null on null with unique keys returning varchar(50));
select json_object('{a, 1, b, "def", c, 3.5}');
select json_object('{{a, 1}, {b, "def"}, {c, 3.5}}');
select json_object('{a,b}', '{1,2}');
/*
 * JSON Processing functions from the Postgres manual
 */
select * from json_array_elements('[1,true, [2,false]]');
select * from json_array_elements_text('["foo", "bar"]');
select json_array_length('[1,2,3,{"f1":1,"f2":[5,6]},4]');
select * from json_each('{"a":"foo", "b":"bar"}');
select * from json_each_text('{"a":"foo", "b":"bar"}');
select json_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}', 'f4', 'f6');
select * from json_object_keys('{"f1":"abc","f2":{"f3":"a", "f4":"b"}}');
select * from json_populate_record(null::myrowtype, '{"a": 1, "b": ["2", "a b"], "c": {"d": 4, "e": "a b c"}, "x": "foo"}');
select * from json_populate_recordset(null::twoints, '[{"a":1,"b":2}, {"a":3,"b":4}]');
select * from json_to_record('{"a":1,"b":[1,2,3],"c":[1,2,3],"e":"bar","r": {"a": 123, "b": "a b c"}}') as x(a int, b text, c int[], d text, r myrowtype);
select * from json_to_recordset('[{"a":1,"b":"foo"}, {"a":"2","c":"bar"}]') as x(a int, b text);
select jsonb_set('[{"f1":1,"f2":null},2,null,3]', '{0,f1}', '[2,3,4]', false);
select jsonb_set_lax('[{"f1":99,"f2":null},2]', '{0,f3}', null, true, 'return_target');
select jsonb_insert('{"a": [0,1,2]}', '{a, 1}', '"new_value"');
select json_strip_nulls('[{"f1":1, "f2":null}, 2, null, 3]');
select jsonb_path_exists('{"a":[1,2,3,4,5]}', '$.a[*] ? (@ >= $min && @ <= $max)', '{"min":2, "max":4}');
select jsonb_path_match('{"a":[1,2,3,4,5]}', 'exists($.a[*] ? (@ >= $min && @ <= $max))', '{"min":2, "max":4}');
select * from jsonb_path_query('{"a":[1,2,3,4,5]}', '$.a[*] ? (@ >= $min && @ <= $max)', '{"min":2, "max":4}');
select jsonb_path_query_array('{"a":[1,2,3,4,5]}', '$.a[*] ? (@ >= $min && @ <= $max)', '{"min":2, "max":4}');
select jsonb_path_query_first('{"a":[1,2,3,4,5]}', '$.a[*] ? (@ >= $min && @ <= $max)', '{"min":2, "max":4}');
select jsonb_path_exists_tz('["2015-08-01 12:00:00-05"]', '$[*] ? (@.datetime() < "2015-08-02".datetime())');
select jsonb_pretty('[{"f1":1,"f2":null}, 2]');
select json_typeof('-123.4');
/*
 * Aggregate functions from the Postgres manual
 */
-- SELECT json_objectagg(k:v) FROM (VALUES ('a'::text,current_date),('b',current_date + 1)) AS t(k,v);
-- SELECT json_objectagg(k:v) over(order by x) FROM (VALUES ('a'::text,current_date),('b',current_date + 1)) AS t(k,v);
SELECT json_arrayagg(v) FROM (VALUES(2),(1)) t(v);
 