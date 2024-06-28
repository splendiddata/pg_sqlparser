/*
 * This file has been altered by SplendidData.
 * It is only used for syntax checking, not for the testing of a commandline paser.
 * So some statements that are expected to fail are removed.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */


--
-- NUMEROLOGY
-- Test various combinations of numeric types and functions.
--


--
-- numeric literals
--

SELECT 0b100101;
SELECT 0o273;
SELECT 0x42F;

-- cases near int4 overflow
SELECT 0b1111111111111111111111111111111;
SELECT 0b10000000000000000000000000000000;
SELECT 0o17777777777;
SELECT 0o20000000000;
SELECT 0x7FFFFFFF;
SELECT 0x80000000;

SELECT -0b10000000000000000000000000000000;
SELECT -0b10000000000000000000000000000001;
SELECT -0o20000000000;
SELECT -0o20000000001;
SELECT -0x80000000;
SELECT -0x80000001;

-- cases near int8 overflow
SELECT 0b111111111111111111111111111111111111111111111111111111111111111;
SELECT 0b1000000000000000000000000000000000000000000000000000000000000000;
SELECT 0o777777777777777777777;
SELECT 0o1000000000000000000000;
SELECT 0x7FFFFFFFFFFFFFFF;
SELECT 0x8000000000000000;

SELECT -0b1000000000000000000000000000000000000000000000000000000000000000;
SELECT -0b1000000000000000000000000000000000000000000000000000000000000001;
SELECT -0o1000000000000000000000;
SELECT -0o1000000000000000000001;
SELECT -0x8000000000000000;
SELECT -0x8000000000000001;

-- error cases
-- Deactivated for SplendidDataTest: SELECT 123abc;
-- Deactivated for SplendidDataTest: SELECT 0x0o;
-- Deactivated for SplendidDataTest: SELECT 0.a;
-- Deactivated for SplendidDataTest: SELECT 0.0a;
-- Deactivated for SplendidDataTest: SELECT .0a;
-- Deactivated for SplendidDataTest: SELECT 0.0e1a;
-- Deactivated for SplendidDataTest: SELECT 0.0e;
-- Deactivated for SplendidDataTest: SELECT 0.0e+a;
-- Deactivated for SplendidDataTest: PREPARE p1 AS SELECT $1a;

-- Deactivated for SplendidDataTest: SELECT 0b;
-- Deactivated for SplendidDataTest: SELECT 1b;
-- Deactivated for SplendidDataTest: SELECT 0b0x;

-- Deactivated for SplendidDataTest: SELECT 0o;
-- Deactivated for SplendidDataTest: SELECT 1o;
-- Deactivated for SplendidDataTest: SELECT 0o0x;

-- Deactivated for SplendidDataTest: SELECT 0x;
-- Deactivated for SplendidDataTest: SELECT 1x;
-- Deactivated for SplendidDataTest: SELECT 0x0y;

-- underscores
SELECT 1_000_000;
SELECT 1_2_3;
SELECT 0x1EEE_FFFF;
SELECT 0o2_73;
SELECT 0b_10_0101;

SELECT 1_000.000_005;
SELECT 1_000.;
SELECT .000_005;
SELECT 1_000.5e0_1;

DO $$
DECLARE
  i int;
BEGIN
  FOR i IN 1_001..1_003 LOOP
    RAISE NOTICE 'i = %', i;
  END LOOP;
END $$;

-- error cases
SELECT _100;
-- Deactivated for SplendidDataTest: SELECT 100_;
-- Deactivated for SplendidDataTest: SELECT 100__000;

-- Deactivated for SplendidDataTest: SELECT _1_000.5;
-- Deactivated for SplendidDataTest: SELECT 1_000_.5;
-- Deactivated for SplendidDataTest: SELECT 1_000._5;
-- Deactivated for SplendidDataTest: SELECT 1_000.5_;
-- Deactivated for SplendidDataTest: SELECT 1_000.5e_1;

-- Deactivated for SplendidDataTest: PREPARE p1 AS SELECT $0_1;

--
-- Test implicit type conversions
-- This fails for Postgres v6.1 (and earlier?)
--  so let's try explicit conversions for now - tgl 97/05/07
--

CREATE TABLE TEMP_FLOAT (f1 FLOAT8);

INSERT INTO TEMP_FLOAT (f1)
  SELECT float8(f1) FROM INT4_TBL;

INSERT INTO TEMP_FLOAT (f1)
  SELECT float8(f1) FROM INT2_TBL;

SELECT f1 FROM TEMP_FLOAT
  ORDER BY f1;

-- int4

CREATE TABLE TEMP_INT4 (f1 INT4);

INSERT INTO TEMP_INT4 (f1)
  SELECT int4(f1) FROM FLOAT8_TBL
  WHERE (f1 > -2147483647) AND (f1 < 2147483647);

INSERT INTO TEMP_INT4 (f1)
  SELECT int4(f1) FROM INT2_TBL;

SELECT f1 FROM TEMP_INT4
  ORDER BY f1;

-- int2

CREATE TABLE TEMP_INT2 (f1 INT2);

INSERT INTO TEMP_INT2 (f1)
  SELECT int2(f1) FROM FLOAT8_TBL
  WHERE (f1 >= -32767) AND (f1 <= 32767);

INSERT INTO TEMP_INT2 (f1)
  SELECT int2(f1) FROM INT4_TBL
  WHERE (f1 >= -32767) AND (f1 <= 32767);

SELECT f1 FROM TEMP_INT2
  ORDER BY f1;

--
-- Group-by combinations
--

CREATE TABLE TEMP_GROUP (f1 INT4, f2 INT4, f3 FLOAT8);

INSERT INTO TEMP_GROUP
  SELECT 1, (- i.f1), (- f.f1)
  FROM INT4_TBL i, FLOAT8_TBL f;

INSERT INTO TEMP_GROUP
  SELECT 2, i.f1, f.f1
  FROM INT4_TBL i, FLOAT8_TBL f;

SELECT DISTINCT f1 AS two FROM TEMP_GROUP ORDER BY 1;

SELECT f1 AS two, max(f3) AS max_float, min(f3) as min_float
  FROM TEMP_GROUP
  GROUP BY f1
  ORDER BY two, max_float, min_float;

-- GROUP BY a result column name is not legal per SQL92, but we accept it
-- anyway (if the name is not the name of any column exposed by FROM).
SELECT f1 AS two, max(f3) AS max_float, min(f3) AS min_float
  FROM TEMP_GROUP
  GROUP BY two
  ORDER BY two, max_float, min_float;

SELECT f1 AS two, (max(f3) + 1) AS max_plus_1, (min(f3) - 1) AS min_minus_1
  FROM TEMP_GROUP
  GROUP BY f1
  ORDER BY two, min_minus_1;

SELECT f1 AS two,
       max(f2) + min(f2) AS max_plus_min,
       min(f3) - 1 AS min_minus_1
  FROM TEMP_GROUP
  GROUP BY f1
  ORDER BY two, min_minus_1;

DROP TABLE TEMP_INT2;

DROP TABLE TEMP_INT4;

DROP TABLE TEMP_FLOAT;

DROP TABLE TEMP_GROUP;
