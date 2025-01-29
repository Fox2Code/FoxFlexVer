package com.fox2code.flexver.test;

import com.fox2code.flexver.FlexVer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlexVerCompareTest {
    private static void checkGreaterThan(String higher, String lower, boolean expected) {
        FlexVer flexVerHigher = FlexVer.parse(higher);
        Assertions.assertEquals(expected, flexVerHigher.isGreater(lower),
                "\"" + higher + "\".isGreaterThan(\"" + lower + "\")");
    }

    @Test
    public void testGreaterThan() {
        checkGreaterThan("1.1.0", "1.0.0", true);
        checkGreaterThan("1.0.0", "1.0.0", false);
        checkGreaterThan("1.0.0", "1.1.0", false);
    }
}
