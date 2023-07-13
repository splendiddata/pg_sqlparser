/*
 * This file has been altered by SplendidData.
 * It is only used for syntax checking, not for the testing of a commandline paser.
 * So some statements that are expected to fail are removed.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */


CREATE TEMP TABLE x (
	a serial,
	b int,
	c text not null default 'stuff',
	d text,
	e text
) ;

CREATE FUNCTION fn_x_before () RETURNS TRIGGER AS '
  BEGIN
		NEW.e := ''before trigger fired''::text;
		return NEW;
	END;
' LANGUAGE plpgsql;

CREATE FUNCTION fn_x_after () RETURNS TRIGGER AS '
  BEGIN
		UPDATE x set e=''after trigger fired'' where c=''stuff'';
		return NULL;
	END;
' LANGUAGE plpgsql;

CREATE TRIGGER trg_x_after AFTER INSERT ON x
FOR EACH ROW EXECUTE PROCEDURE fn_x_after();

CREATE TRIGGER trg_x_before BEFORE INSERT ON x
FOR EACH ROW EXECUTE PROCEDURE fn_x_before();

COPY x (a, b, c, d, e) from stdin;
-- Deactivated for SplendidDataTest: 9999	\N	\\N	\NN	\N
-- Deactivated for SplendidDataTest: 10000	21	31	41	51
-- Deactivated for SplendidDataTest: \.

COPY x (b, d) from stdin;
-- Deactivated for SplendidDataTest: 1	test_1
-- Deactivated for SplendidDataTest: \.

COPY x (b, d) from stdin;
-- Deactivated for SplendidDataTest: 2	test_2
-- Deactivated for SplendidDataTest: 3	test_3
-- Deactivated for SplendidDataTest: 4	test_4
-- Deactivated for SplendidDataTest: 5	test_5
-- Deactivated for SplendidDataTest: \.

COPY x (a, b, c, d, e) from stdin;
-- Deactivated for SplendidDataTest: 10001	22	32	42	52
-- Deactivated for SplendidDataTest: 10002	23	33	43	53
-- Deactivated for SplendidDataTest: 10003	24	34	44	54
-- Deactivated for SplendidDataTest: 10004	25	35	45	55
-- Deactivated for SplendidDataTest: 10005	26	36	46	56
-- Deactivated for SplendidDataTest: \.

-- non-existent column in column list: should fail
COPY x (xyz) from stdin;

-- redundant options
COPY x from stdin (format CSV, FORMAT CSV);
COPY x from stdin (freeze off, freeze on);
COPY x from stdin (delimiter ',', delimiter ',');
COPY x from stdin (null ' ', null ' ');
COPY x from stdin (header off, header on);
COPY x from stdin (quote ':', quote ':');
COPY x from stdin (escape ':', escape ':');
COPY x from stdin (force_quote (a), force_quote *);
COPY x from stdin (force_not_null (a), force_not_null (b));
COPY x from stdin (force_null (a), force_null (b));
COPY x from stdin (convert_selectively (a), convert_selectively (b));
COPY x from stdin (encoding 'sql_ascii', encoding 'sql_ascii');

-- incorrect options
COPY x to stdin (format BINARY, delimiter ',');
COPY x to stdin (format BINARY, null 'x');
COPY x to stdin (format TEXT, force_quote(a));
COPY x from stdin (format CSV, force_quote(a));
COPY x to stdout (format TEXT, force_not_null(a));
COPY x to stdin (format CSV, force_not_null(a));
COPY x to stdout (format TEXT, force_null(a));
COPY x to stdin (format CSV, force_null(a));

-- too many columns in column list: should fail
COPY x (a, b, c, d, e, d, c) from stdin;

-- missing data: should fail
COPY x from stdin;

\.
COPY x from stdin;
-- Deactivated for SplendidDataTest: 2000	230	23	23
-- Deactivated for SplendidDataTest: \.
COPY x from stdin;
-- Deactivated for SplendidDataTest: 2001	231	\N	\N
-- Deactivated for SplendidDataTest: \.

-- extra data: should fail
COPY x from stdin;
-- Deactivated for SplendidDataTest: 2002	232	40	50	60	70	80
-- Deactivated for SplendidDataTest: \.

-- various COPY options: delimiters, oids, NULL string, encoding
COPY x (b, c, d, e) from stdin delimiter ',' null 'x';
-- Deactivated for SplendidDataTest: x,45,80,90
-- Deactivated for SplendidDataTest: x,\x,\\x,\\\x
-- Deactivated for SplendidDataTest: x,\,,\\\,,\\
-- Deactivated for SplendidDataTest: \.

COPY x from stdin WITH DELIMITER AS ';' NULL AS '';
-- Deactivated for SplendidDataTest: 3000;;c;;
-- Deactivated for SplendidDataTest: \.

COPY x from stdin WITH DELIMITER AS ':' NULL AS E'\\X' ENCODING 'sql_ascii';
-- Deactivated for SplendidDataTest: 4000:\X:C:\X:\X
-- Deactivated for SplendidDataTest: 4001:1:empty::
-- Deactivated for SplendidDataTest: 4002:2:null:\X:\X
-- Deactivated for SplendidDataTest: 4003:3:Backslash:\\:\\
-- Deactivated for SplendidDataTest: 4004:4:BackslashX:\\X:\\X
-- Deactivated for SplendidDataTest: 4005:5:N:\N:\N
-- Deactivated for SplendidDataTest: 4006:6:BackslashN:\\N:\\N
-- Deactivated for SplendidDataTest: 4007:7:XX:\XX:\XX
-- Deactivated for SplendidDataTest: 4008:8:Delimiter:\::\:
-- Deactivated for SplendidDataTest: \.

COPY x TO stdout WHERE a = 1;
COPY x from stdin WHERE a = 50004;
-- Deactivated for SplendidDataTest: 50003	24	34	44	54
-- Deactivated for SplendidDataTest: 50004	25	35	45	55
-- Deactivated for SplendidDataTest: 50005	26	36	46	56
-- Deactivated for SplendidDataTest: \.

COPY x from stdin WHERE a > 60003;
-- Deactivated for SplendidDataTest: 60001	22	32	42	52
-- Deactivated for SplendidDataTest: 60002	23	33	43	53
-- Deactivated for SplendidDataTest: 60003	24	34	44	54
-- Deactivated for SplendidDataTest: 60004	25	35	45	55
-- Deactivated for SplendidDataTest: 60005	26	36	46	56
-- Deactivated for SplendidDataTest: \.

COPY x from stdin WHERE f > 60003;

COPY x from stdin WHERE a = max(x.b);

COPY x from stdin WHERE a IN (SELECT 1 FROM x);

COPY x from stdin WHERE a IN (generate_series(1,5));

COPY x from stdin WHERE a = row_number() over(b);


-- check results of copy in
SELECT * FROM x;

-- check copy out
COPY x TO stdout;
COPY x (c, e) TO stdout;
COPY x (b, e) TO stdout WITH NULL 'I''m null';

CREATE TEMP TABLE y (
	col1 text,
	col2 text
);

INSERT INTO y VALUES ('Jackson, Sam', E'\\h');
INSERT INTO y VALUES ('It is "perfect".',E'\t');
INSERT INTO y VALUES ('', NULL);

COPY y TO stdout WITH CSV;
COPY y TO stdout WITH CSV QUOTE '''' DELIMITER '|';
COPY y TO stdout WITH CSV FORCE QUOTE col2 ESCAPE E'\\' ENCODING 'sql_ascii';
COPY y TO stdout WITH CSV FORCE QUOTE *;

-- Repeat above tests with new 9.0 option syntax

COPY y TO stdout (FORMAT CSV);
COPY y TO stdout (FORMAT CSV, QUOTE '''', DELIMITER '|');
COPY y TO stdout (FORMAT CSV, FORCE_QUOTE (col2), ESCAPE E'\\');
COPY y TO stdout (FORMAT CSV, FORCE_QUOTE *);

\copy y TO stdout (FORMAT CSV)
\copy y TO stdout (FORMAT CSV, QUOTE '''', DELIMITER '|')
\copy y TO stdout (FORMAT CSV, FORCE_QUOTE (col2), ESCAPE E'\\')
\copy y TO stdout (FORMAT CSV, FORCE_QUOTE *)

--test that we read consecutive LFs properly

CREATE TEMP TABLE testnl (a int, b text, c int);

COPY testnl FROM stdin CSV;
-- Deactivated for SplendidDataTest: 1,"a field with two LFs

-- Deactivated for SplendidDataTest: inside",2
-- Deactivated for SplendidDataTest: \.

-- test end of copy marker
CREATE TEMP TABLE testeoc (a text);

COPY testeoc FROM stdin CSV;
-- Deactivated for SplendidDataTest: a\.
-- Deactivated for SplendidDataTest: \.b
-- Deactivated for SplendidDataTest: c\.d
-- Deactivated for SplendidDataTest: "\."
-- Deactivated for SplendidDataTest: \.

COPY testeoc TO stdout CSV;

-- test handling of nonstandard null marker that violates escaping rules

CREATE TEMP TABLE testnull(a int, b text);
INSERT INTO testnull VALUES (1, E'\\0'), (NULL, NULL);

COPY testnull TO stdout WITH NULL AS E'\\0';

COPY testnull FROM stdin WITH NULL AS E'\\0';
-- Deactivated for SplendidDataTest: 42	\\0
-- Deactivated for SplendidDataTest: \0	\0
-- Deactivated for SplendidDataTest: \.

SELECT * FROM testnull;

BEGIN;
CREATE TABLE vistest (LIKE testeoc);
COPY vistest FROM stdin CSV;
-- Deactivated for SplendidDataTest: a0
-- Deactivated for SplendidDataTest: b
-- Deactivated for SplendidDataTest: \.
COMMIT;
SELECT * FROM vistest;
BEGIN;
TRUNCATE vistest;
COPY vistest FROM stdin CSV;
-- Deactivated for SplendidDataTest: a1
-- Deactivated for SplendidDataTest: b
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
SAVEPOINT s1;
TRUNCATE vistest;
COPY vistest FROM stdin CSV;
-- Deactivated for SplendidDataTest: d1
-- Deactivated for SplendidDataTest: e
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
COMMIT;
SELECT * FROM vistest;

BEGIN;
TRUNCATE vistest;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: a2
-- Deactivated for SplendidDataTest: b
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
SAVEPOINT s1;
TRUNCATE vistest;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: d2
-- Deactivated for SplendidDataTest: e
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
COMMIT;
SELECT * FROM vistest;

BEGIN;
TRUNCATE vistest;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: x
-- Deactivated for SplendidDataTest: y
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
COMMIT;
TRUNCATE vistest;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: p
-- Deactivated for SplendidDataTest: g
-- Deactivated for SplendidDataTest: \.
BEGIN;
TRUNCATE vistest;
SAVEPOINT s1;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: m
-- Deactivated for SplendidDataTest: k
-- Deactivated for SplendidDataTest: \.
COMMIT;
BEGIN;
INSERT INTO vistest VALUES ('z');
SAVEPOINT s1;
TRUNCATE vistest;
ROLLBACK TO SAVEPOINT s1;
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: d3
-- Deactivated for SplendidDataTest: e
-- Deactivated for SplendidDataTest: \.
COMMIT;
CREATE FUNCTION truncate_in_subxact() RETURNS VOID AS
$$
BEGIN
	TRUNCATE vistest;
EXCEPTION
  WHEN OTHERS THEN
	INSERT INTO vistest VALUES ('subxact failure');
END;
$$ language plpgsql;
BEGIN;
INSERT INTO vistest VALUES ('z');
SELECT truncate_in_subxact();
COPY vistest FROM stdin CSV FREEZE;
-- Deactivated for SplendidDataTest: d4
-- Deactivated for SplendidDataTest: e
-- Deactivated for SplendidDataTest: \.
SELECT * FROM vistest;
COMMIT;
SELECT * FROM vistest;
-- Test FORCE_NOT_NULL and FORCE_NULL options
CREATE TEMP TABLE forcetest (
    a INT NOT NULL,
    b TEXT NOT NULL,
    c TEXT,
    d TEXT,
    e TEXT
);
\pset null NULL
-- should succeed with no effect ("b" remains an empty string, "c" remains NULL)
BEGIN;
COPY forcetest (a, b, c) FROM STDIN WITH (FORMAT csv, FORCE_NOT_NULL(b), FORCE_NULL(c));
-- Deactivated for SplendidDataTest: 1,,""
-- Deactivated for SplendidDataTest: \.
COMMIT;
SELECT b, c FROM forcetest WHERE a = 1;
-- should succeed, FORCE_NULL and FORCE_NOT_NULL can be both specified
BEGIN;
COPY forcetest (a, b, c, d) FROM STDIN WITH (FORMAT csv, FORCE_NOT_NULL(c,d), FORCE_NULL(c,d));
-- Deactivated for SplendidDataTest: 2,'a',,""
-- Deactivated for SplendidDataTest: \.
COMMIT;
SELECT c, d FROM forcetest WHERE a = 2;
-- should fail with not-null constraint violation
BEGIN;
COPY forcetest (a, b, c) FROM STDIN WITH (FORMAT csv, FORCE_NULL(b), FORCE_NOT_NULL(c));
-- Deactivated for SplendidDataTest: 3,,""
-- Deactivated for SplendidDataTest: \.
ROLLBACK;
-- should fail with "not referenced by COPY" error
BEGIN;
COPY forcetest (d, e) FROM STDIN WITH (FORMAT csv, FORCE_NOT_NULL(b));
ROLLBACK;
-- should fail with "not referenced by COPY" error
BEGIN;
COPY forcetest (d, e) FROM STDIN WITH (FORMAT csv, FORCE_NULL(b));
ROLLBACK;
\pset null ''

-- test case with whole-row Var in a check constraint
create table check_con_tbl (f1 int);
create function check_con_function(check_con_tbl) returns bool as $$
begin
  raise notice 'input = %', row_to_json($1);
  return $1.f1 > 0;
end $$ language plpgsql immutable;
alter table check_con_tbl add check (check_con_function(check_con_tbl.*));
\d+ check_con_tbl
copy check_con_tbl from stdin;
-- Deactivated for SplendidDataTest: 1
-- Deactivated for SplendidDataTest: \N
-- Deactivated for SplendidDataTest: \.
copy check_con_tbl from stdin;
-- Deactivated for SplendidDataTest: 0
-- Deactivated for SplendidDataTest: \.
select * from check_con_tbl;

-- test with RLS enabled.
CREATE ROLE regress_rls_copy_user;
CREATE ROLE regress_rls_copy_user_colperms;
CREATE TABLE rls_t1 (a int, b int, c int);

COPY rls_t1 (a, b, c) from stdin;
-- Deactivated for SplendidDataTest: 1	4	1
-- Deactivated for SplendidDataTest: 2	3	2
-- Deactivated for SplendidDataTest: 3	2	3
-- Deactivated for SplendidDataTest: 4	1	4
-- Deactivated for SplendidDataTest: \.

CREATE POLICY p1 ON rls_t1 FOR SELECT USING (a % 2 = 0);
ALTER TABLE rls_t1 ENABLE ROW LEVEL SECURITY;
ALTER TABLE rls_t1 FORCE ROW LEVEL SECURITY;

GRANT SELECT ON TABLE rls_t1 TO regress_rls_copy_user;
GRANT SELECT (a, b) ON TABLE rls_t1 TO regress_rls_copy_user_colperms;

-- all columns
COPY rls_t1 TO stdout;
COPY rls_t1 (a, b, c) TO stdout;

-- subset of columns
COPY rls_t1 (a) TO stdout;
COPY rls_t1 (a, b) TO stdout;

-- column reordering
COPY rls_t1 (b, a) TO stdout;

SET SESSION AUTHORIZATION regress_rls_copy_user;

-- all columns
COPY rls_t1 TO stdout;
COPY rls_t1 (a, b, c) TO stdout;

-- subset of columns
COPY rls_t1 (a) TO stdout;
COPY rls_t1 (a, b) TO stdout;

-- column reordering
COPY rls_t1 (b, a) TO stdout;

RESET SESSION AUTHORIZATION;

SET SESSION AUTHORIZATION regress_rls_copy_user_colperms;

-- attempt all columns (should fail)
COPY rls_t1 TO stdout;
COPY rls_t1 (a, b, c) TO stdout;

-- try to copy column with no privileges (should fail)
COPY rls_t1 (c) TO stdout;

-- subset of columns (should succeed)
COPY rls_t1 (a) TO stdout;
COPY rls_t1 (a, b) TO stdout;

RESET SESSION AUTHORIZATION;

-- test with INSTEAD OF INSERT trigger on a view
CREATE TABLE instead_of_insert_tbl(id serial, name text);
CREATE VIEW instead_of_insert_tbl_view AS SELECT ''::text AS str;

COPY instead_of_insert_tbl_view FROM stdin; -- fail
-- Deactivated for SplendidDataTest: test1
-- Deactivated for SplendidDataTest: \.

CREATE FUNCTION fun_instead_of_insert_tbl() RETURNS trigger AS $$
BEGIN
  INSERT INTO instead_of_insert_tbl (name) VALUES (NEW.str);
  RETURN NULL;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER trig_instead_of_insert_tbl_view
  INSTEAD OF INSERT ON instead_of_insert_tbl_view
  FOR EACH ROW EXECUTE PROCEDURE fun_instead_of_insert_tbl();

COPY instead_of_insert_tbl_view FROM stdin;
-- Deactivated for SplendidDataTest: test1
-- Deactivated for SplendidDataTest: \.

SELECT * FROM instead_of_insert_tbl;

-- Test of COPY optimization with view using INSTEAD OF INSERT
-- trigger when relation is created in the same transaction as
-- when COPY is executed.
BEGIN;
CREATE VIEW instead_of_insert_tbl_view_2 as select ''::text as str;
CREATE TRIGGER trig_instead_of_insert_tbl_view_2
  INSTEAD OF INSERT ON instead_of_insert_tbl_view_2
  FOR EACH ROW EXECUTE PROCEDURE fun_instead_of_insert_tbl();

COPY instead_of_insert_tbl_view_2 FROM stdin;
-- Deactivated for SplendidDataTest: test1
-- Deactivated for SplendidDataTest: \.

SELECT * FROM instead_of_insert_tbl;
COMMIT;

-- clean up
DROP TABLE forcetest;
DROP TABLE vistest;
DROP FUNCTION truncate_in_subxact();
DROP TABLE x, y;
DROP TABLE rls_t1 CASCADE;
DROP ROLE regress_rls_copy_user;
DROP ROLE regress_rls_copy_user_colperms;
DROP FUNCTION fn_x_before();
DROP FUNCTION fn_x_after();
DROP TABLE instead_of_insert_tbl;
DROP VIEW instead_of_insert_tbl_view;
DROP VIEW instead_of_insert_tbl_view_2;
DROP FUNCTION fun_instead_of_insert_tbl();

--
-- COPY FROM ... DEFAULT
--

create temp table copy_default (
	id integer primary key,
	text_value text not null default 'test',
	ts_value timestamp without time zone not null default '2022-07-05'
);

-- if DEFAULT is not specified, then the marker will be regular data
copy copy_default from stdin;
-- Deactivated for SplendidDataTest: 1	value	'2022-07-04'
-- Deactivated for SplendidDataTest: 2	\D	'2022-07-05'
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

copy copy_default from stdin with (format csv);
-- Deactivated for SplendidDataTest: 1,value,2022-07-04
-- Deactivated for SplendidDataTest: 2,\D,2022-07-05
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

-- DEFAULT cannot be used in binary mode
copy copy_default from stdin with (format binary, default '\D');

-- DEFAULT cannot be new line nor carriage return
copy copy_default from stdin with (default E'\n');
copy copy_default from stdin with (default E'\r');

-- DELIMITER cannot appear in DEFAULT spec
copy copy_default from stdin with (delimiter ';', default 'test;test');

-- CSV quote cannot appear in DEFAULT spec
copy copy_default from stdin with (format csv, quote '"', default 'test"test');

-- NULL and DEFAULT spec must be different
copy copy_default from stdin with (default '\N');

-- cannot use DEFAULT marker in column that has no DEFAULT value
copy copy_default from stdin with (default '\D');
-- Deactivated for SplendidDataTest: \D	value	'2022-07-04'
-- Deactivated for SplendidDataTest: 2	\D	'2022-07-05'
-- Deactivated for SplendidDataTest: \.

copy copy_default from stdin with (format csv, default '\D');
-- Deactivated for SplendidDataTest: \D,value,2022-07-04
-- Deactivated for SplendidDataTest: 2,\D,2022-07-05
-- Deactivated for SplendidDataTest: \.

-- The DEFAULT marker must be unquoted and unescaped or it's not recognized
copy copy_default from stdin with (default '\D');
-- Deactivated for SplendidDataTest: 1	\D	'2022-07-04'
-- Deactivated for SplendidDataTest: 2	\\D	'2022-07-04'
-- Deactivated for SplendidDataTest: 3	"\D"	'2022-07-04'
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

copy copy_default from stdin with (format csv, default '\D');
-- Deactivated for SplendidDataTest: 1,\D,2022-07-04
-- Deactivated for SplendidDataTest: 2,\\D,2022-07-04
-- Deactivated for SplendidDataTest: 3,"\D",2022-07-04
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

-- successful usage of DEFAULT option in COPY
copy copy_default from stdin with (default '\D');
-- Deactivated for SplendidDataTest: 1	value	'2022-07-04'
-- Deactivated for SplendidDataTest: 2	\D	'2022-07-03'
-- Deactivated for SplendidDataTest: 3	\D	\D
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

copy copy_default from stdin with (format csv, default '\D');
-- Deactivated for SplendidDataTest: 1,value,2022-07-04
-- Deactivated for SplendidDataTest: 2,\D,2022-07-03
-- Deactivated for SplendidDataTest: 3,\D,\D
-- Deactivated for SplendidDataTest: \.

select id, text_value, ts_value from copy_default;

truncate copy_default;

-- DEFAULT cannot be used in COPY TO
copy (select 1 as test) TO stdout with (default '\D');
