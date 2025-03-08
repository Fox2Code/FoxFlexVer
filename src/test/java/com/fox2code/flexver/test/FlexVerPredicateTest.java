package com.fox2code.flexver.test;

import com.fox2code.flexver.FlexVerPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlexVerPredicateTest {
    private static void checkPredicate(String predicate,String version, boolean expected) {
        FlexVerPredicate flexVerPredicate = FlexVerPredicate.parse(predicate);
        Assertions.assertEquals(expected, flexVerPredicate.match(version),
                "\"" + predicate + "\".match(\"" + version + "\")");
    }

    @Test
    public void testComparing() {
        checkPredicate("1.0.0=", "1.0.0+test2.0", true);
        checkPredicate("1.0.0>=", "0.0.1", false);
        checkPredicate("1.0.0>=", "1.0.0", true);
        checkPredicate("1.0.0>=", "1.1.0", true);
        checkPredicate("1.0.0>=", "2.0.0", true);
        checkPredicate("1.0.0>", "1.0.0", false);
        checkPredicate("1.0.0>", "1.0.1", true);
        checkPredicate("1.0.0>", "2.0.0", true);
        checkPredicate("1.0.*", "1.0.0", true);
        checkPredicate("1.0.*", "1.0.1", true);
        checkPredicate("1.0.*", "1.1.0", false);
        checkPredicate("1.0.*", "2.0.0", false);
    }

    @Test
    public void testComparingWildcard() {
        checkPredicate("*", "1.0.0", true);
        checkPredicate("*", "0.0.0", true);
        checkPredicate("*", "", true);
    }
}
