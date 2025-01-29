package com.fox2code.flexver.components;

public final class PreReleaseVersionComponent extends VersionComponent {
    public static PreReleaseVersionComponent make(int[] codepoints) {
        return new PreReleaseVersionComponent(codepoints);
    }

    PreReleaseVersionComponent(int[] codepoints) { super(codepoints); }

    @Override
    public int compareTo(VersionComponent that) {
        if (that == NULL) return -1; // opposite order
        return super.compareTo(that);
    }
}
