
package com.splendiddata.sqlparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractCProgramTest {
    @Test
    public void strcmp() {
        String str1 = "permissive";
        String str2 = "something else";
        int result = AbstractCProgram.strcmp(str1, str2);
        Assertions.assertTrue(result < 0, "strcmp(" + str1 + ", " + str2 + " returned " + result + ", expected result < 0");
        str2 = str1;
        result = AbstractCProgram.strcmp(str1, str2);
        Assertions.assertTrue(result == 0, "strcmp(" + str1 + ", " + str2 + " returned " + result + ", expected result = 0");
        str2 = "another string";
        result = AbstractCProgram.strcmp(str1, str2);
        Assertions.assertTrue(result > 0, "strcmp(" + str1 + ", " + str2 + " returned " + result + ", expected result > 0");
    }
}
