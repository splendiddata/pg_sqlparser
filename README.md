# pg_sqlparser
**An sql parser that parses PostgreSQL to Java objects**

Sometimes it may be handy to be able to parse (PostgreSQL) sql statements into Java objects. pg_sqlparser does that.

The parser is generated directly from the actual PostgreSQL parser sources, so the implementation should come pretty close to what PostgreSQL does itself.

## Features
* Parses PostgreSQL sql statements to a Java object structure
* Can visualise the parsed structure in an XML structure
* Can generate sql statements from the Java object structure
* Comes with a test GUI to visualise the three features above

## Prerequisites
* Java 11 or higher
* Maven 3.5.2 or higher
* Bison 3.0 or higher

## Usage
Here is an example of how to use the parser:

```
   ...
public static void main(String[] args) {
    String sql = "SELECT a, sum(b) AS total "
               + "FROM tbl "
               + "WHERE a != 'excl' "
               + "GROUP BY a";
    List<SqlParserErrorData> parserErrors = new ArrayList<>();
    try (Reader reader = new StringReader(sql)) {
        SqlParser parser = new SqlParser(reader
                                        , error -> parserErrors.add(error)
                                        );
        if (parser.parse()) {
            for (Node parsedStatement : parser.getResult()) {
                System.out.println("The parsed statement = "
                                  + parsedStatement
                                  );
                System.out.println(
                        "The statement in XML format looks like:\n"
                        + ParserUtil.stmtToXml(parsedStatement)
                        );
            }
        } else {
            for (SqlParserErrorData err : parserErrors) {
                System.out.println("Parser error: " + err);
            }
        }
    } catch (IOException e) {
        System.out.println(e);
    }
}
   ...
```
More information can be found in [https://github.com/splendiddata/pg_sqlparser/tree/main/doc/README.pdf](https://github.com/splendiddata/pg_sqlparser/tree/main/doc/README.pdf)

##branches
The Postgres_13 branch contains a parser based on the PostgreSQL 13 parser.<br>
The main branch is based on the PostgreSQL 14 parser.
