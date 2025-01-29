package com.fox2code.flexver.components;

import java.util.Arrays;

public final class NumericVersionComponent extends VersionComponent {
    private static final NumericVersionComponent[] CACHE = new NumericVersionComponent[10];

    static {
        for (int i = 0; i < 10; i++) {
            CACHE[i] = new NumericVersionComponent(new int[]{'0' + i});
        }
    }

    public static NumericVersionComponent make(int[] codepoints) {
        if (codepoints.length == 1) {
            int codepoint = codepoints[0];
            if (codepoint >= '0' && codepoint <= '9') {
                return CACHE[codepoint - '0'];
            }
        }
        return new NumericVersionComponent(codepoints);
    }

    private NumericVersionComponent(int[] codepoints) {
        super(codepoints);
    }

    @Override
    public int compareTo(VersionComponent that) {
        if (that == NULL) return 1;
        if (this == that) return 0;
        if (that instanceof NumericVersionComponent) {
            int[] a = removeLeadingZeroes(this.codepoints());
            int[] b = removeLeadingZeroes(that.codepoints());
            if (a.length != b.length) return a.length-b.length;
            for (int i = 0; i < a.length; i++) {
                int ad = a[i];
                int bd = b[i];
                if (ad != bd) return ad-bd;
            }
            return 0;
        }
        return super.compareTo(that);
    }

    private int[] removeLeadingZeroes(int[] a) {
        if (a.length == 1) return a;
        int i = 0;
        int stopIdx = a.length - 1;
        while (i < stopIdx && a[i] == '0') {
            i++;
        }
        return Arrays.copyOfRange(a, i, a.length);
    }
}
