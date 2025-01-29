package com.fox2code.flexver.components;

import java.util.Arrays;

public class VersionComponent implements Comparable<VersionComponent> {
    public static final VersionComponent EMPTY = new VersionComponent(new int[0]);
    public static final VersionComponent NULL = new VersionComponent(new int[0]) {
        @Override
        public int compareTo(VersionComponent other) { return other == NULL ? 0 : -other.compareTo(this); }
    };
    private final int[] codepoints;

    public static VersionComponent make(int[] codepoints) {
        if (codepoints.length == 0) return EMPTY;
        return new VersionComponent(codepoints);
    }

    VersionComponent(int[] codepoints) {
        this.codepoints = codepoints;
    }

    int[] codepoints() {
        return codepoints;
    }

    @Override
    public int compareTo(VersionComponent that) {
        if (that == NULL) return 1;
        int[] a = this.codepoints();
        int[] b = that.codepoints();

        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            int c1 = a[i];
            int c2 = b[i];
            if (c1 != c2) return c1 - c2;
        }

        return a.length - b.length;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.codepoints);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && this.getClass() == obj.getClass() &&
                Arrays.equals(this.codepoints, ((VersionComponent) obj).codepoints);
    }

    @Override
    public String toString() {
        return new String(codepoints, 0, codepoints.length);
    }

    public void recompose(StringBuilder stringBuilder, boolean withDot) {
        for (int codepoint : this.codepoints) {
            stringBuilder.appendCodePoint(codepoint);
        }
    }
}