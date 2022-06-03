/*
 * This file has been altered by SplendidData.
 * It is only used for syntax checking, not for the testing of a commandline paser.
 * So input for the copy statements is removed.
 * The deactivated lines are marked by: -- Deactivated for SplendidDataTest: 
 */


--
-- \crosstabview
--

CREATE TABLE ctv_data (v, h, c, i, d) AS
VALUES
   ('v1','h2','foo', 3, '2015-04-01'::date),
   ('v2','h1','bar', 3, '2015-01-02'),
   ('v1','h0','baz', NULL, '2015-07-12'),
   ('v0','h4','qux', 4, '2015-07-15'),
   ('v0','h4','dbl', -3, '2014-12-15'),
   ('v0',NULL,'qux', 5, '2014-07-15'),
   ('v1','h2','quux',7, '2015-04-04');

-- make plans more stable
ANALYZE ctv_data;

-- running \crosstabview after query uses query in buffer
SELECT v, EXTRACT(year FROM d), count(*)
 FROM ctv_data
 GROUP BY 1, 2
 ORDER BY 1, 2;
-- basic usage with 3 columns
-- Deactivated for SplendidDataTest:  \crosstabview

-- ordered months in horizontal header, quoted column name
-- Deactivated for SplendidDataTest: SELECT v, to_char(d, 'Mon') AS "month name", EXTRACT(month FROM d) AS num,
 -- Deactivated for SplendidDataTest: count(*) FROM ctv_data  GROUP BY 1,2,3 ORDER BY 1
-- Deactivated for SplendidDataTest:  \crosstabview v "month name" 4 num

-- ordered months in vertical header, ordered years in horizontal header
-- Deactivated for SplendidDataTest: SELECT EXTRACT(year FROM d) AS year, to_char(d,'Mon') AS """month"" name",
-- Deactivated for SplendidDataTest:   EXTRACT(month FROM d) AS month,
-- Deactivated for SplendidDataTest:   format('sum=%s avg=%s', sum(i), avg(i)::numeric(2,1))
-- Deactivated for SplendidDataTest:   FROM ctv_data
-- Deactivated for SplendidDataTest:   GROUP BY EXTRACT(year FROM d), to_char(d,'Mon'), EXTRACT(month FROM d)
-- Deactivated for SplendidDataTest: ORDER BY month
-- Deactivated for SplendidDataTest: \crosstabview """month"" name" year format year

-- combine contents vertically into the same cell (V/H duplicates)
-- Deactivated for SplendidDataTest: SELECT v, h, string_agg(c, E'\n') FROM ctv_data GROUP BY v, h ORDER BY 1,2,3
-- Deactivated for SplendidDataTest:  \crosstabview 1 2 3

-- horizontal ASC order from window function
-- Deactivated for SplendidDataTest: SELECT v,h, string_agg(c, E'\n') AS c, row_number() OVER(ORDER BY h) AS r
-- Deactivated for SplendidDataTest: FROM ctv_data GROUP BY v, h ORDER BY 1,3,2
-- Deactivated for SplendidDataTest:  \crosstabview v h c r

-- horizontal DESC order from window function
-- Deactivated for SplendidDataTest: SELECT v, h, string_agg(c, E'\n') AS c, row_number() OVER(ORDER BY h DESC) AS r
-- Deactivated for SplendidDataTest: FROM ctv_data GROUP BY v, h ORDER BY 1,3,2
-- Deactivated for SplendidDataTest:  \crosstabview v h c r

-- horizontal ASC order from window function, NULLs pushed rightmost
-- Deactivated for SplendidDataTest: SELECT v,h, string_agg(c, E'\n') AS c, row_number() OVER(ORDER BY h NULLS LAST) AS r
-- Deactivated for SplendidDataTest: FROM ctv_data GROUP BY v, h ORDER BY 1,3,2
-- Deactivated for SplendidDataTest:  \crosstabview v h c r

-- only null, no column name, 2 columns: error
-- Deactivated for SplendidDataTest: SELECT null,null \crosstabview

-- only null, no column name, 3 columns: works
-- Deactivated for SplendidDataTest: SELECT null,null,null \crosstabview

-- null display
-- Deactivated for SplendidDataTest: \pset null '#null#'
-- Deactivated for SplendidDataTest: SELECT v,h, string_agg(i::text, E'\n') AS i FROM ctv_data
-- Deactivated for SplendidDataTest: GROUP BY v, h ORDER BY h,v
-- Deactivated for SplendidDataTest:  \crosstabview v h i
-- Deactivated for SplendidDataTest: \pset null ''

-- refer to columns by position
-- Deactivated for SplendidDataTest: SELECT v,h,string_agg(i::text, E'\n'), string_agg(c, E'\n')
-- Deactivated for SplendidDataTest: FROM ctv_data GROUP BY v, h ORDER BY h,v
-- Deactivated for SplendidDataTest:  \crosstabview 2 1 4

-- refer to columns by positions and names mixed
-- Deactivated for SplendidDataTest: SELECT v,h, string_agg(i::text, E'\n') AS i, string_agg(c, E'\n') AS c
-- Deactivated for SplendidDataTest: FROM ctv_data GROUP BY v, h ORDER BY h,v
-- Deactivated for SplendidDataTest:  \crosstabview 1 "h" 4

-- refer to columns by quoted names, check downcasing of unquoted name
-- Deactivated for SplendidDataTest: SELECT 1 as "22", 2 as b, 3 as "Foo"
-- Deactivated for SplendidDataTest:  \crosstabview "22" B "Foo"

-- error: bad column name
-- Deactivated for SplendidDataTest: SELECT v,h,c,i FROM ctv_data
-- Deactivated for SplendidDataTest:  \crosstabview v h j

-- error: need to quote name
-- Deactivated for SplendidDataTest: SELECT 1 as "22", 2 as b, 3 as "Foo"
-- Deactivated for SplendidDataTest:  \crosstabview 1 2 Foo

-- error: need to not quote name
-- Deactivated for SplendidDataTest: SELECT 1 as "22", 2 as b, 3 as "Foo"
-- Deactivated for SplendidDataTest:  \crosstabview 1 "B" "Foo"

-- error: bad column number
-- Deactivated for SplendidDataTest: SELECT v,h,i,c FROM ctv_data
-- Deactivated for SplendidDataTest:  \crosstabview 2 1 5

-- error: same H and V columns
-- Deactivated for SplendidDataTest: SELECT v,h,i,c FROM ctv_data
-- Deactivated for SplendidDataTest:  \crosstabview 2 h 4

-- error: too many columns
-- Deactivated for SplendidDataTest: SELECT a,a,1 FROM generate_series(1,3000) AS a
-- Deactivated for SplendidDataTest:  \crosstabview

-- error: only one column
-- Deactivated for SplendidDataTest: SELECT 1 \crosstabview

DROP TABLE ctv_data;

-- check error reporting (bug #14476)
CREATE TABLE ctv_data (x int, y int, v text);

INSERT INTO ctv_data SELECT 1, x, '*' || x FROM generate_series(1,10) x;
-- Deactivated for SplendidDataTest: SELECT * FROM ctv_data \crosstabview

INSERT INTO ctv_data VALUES (1, 10, '*'); -- duplicate data to cause error
-- Deactivated for SplendidDataTest: SELECT * FROM ctv_data \crosstabview

DROP TABLE ctv_data;
