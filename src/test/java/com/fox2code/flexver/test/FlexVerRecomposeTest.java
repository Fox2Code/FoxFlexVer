package com.fox2code.flexver.test;

import com.fox2code.flexver.FlexVerPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlexVerRecomposeTest {
    public static void checkRecomposeExact(String flexVer) {
        Assertions.assertEquals(flexVer, FlexVerPredicate.parse(flexVer).toString());
    }

    public static void checkRecompose(String result, String flexVer) {
        Assertions.assertEquals(result, FlexVerPredicate.parse(flexVer).toString());
    }

    @Test
    public void testRecompose() {
        checkRecomposeExact("1.0.0");
        checkRecomposeExact("1.0.*");
        checkRecomposeExact("1.0>=");
        checkRecomposeExact("1.0.0-test2.0");
        checkRecompose("1.0.0", "1.0.0+test2.0");
        checkRecompose("1.0.0", "1.0.0+test2.0.*");
        checkRecompose("1.0.0>=", "1.0.0+test2.0>=");
    }
}
