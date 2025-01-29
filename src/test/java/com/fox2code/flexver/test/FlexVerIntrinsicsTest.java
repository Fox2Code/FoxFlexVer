package com.fox2code.flexver.test;

import com.fox2code.flexver.FlexVer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FlexVerIntrinsicsTest {
    private static void checkEquality(String version) {
        Assertions.assertEquals(FlexVer.parse(version), FlexVer.parse(version));
    }

    @Test
    public void testEquality() {
        checkEquality("");
        checkEquality("1.0.0");
        checkEquality("1.0.0-test2.0");
        checkEquality("1.0.0+test2.0");
    }
}
