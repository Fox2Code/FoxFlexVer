package com.fox2code.flexver;

import com.fox2code.flexver.components.NumericVersionComponent;
import com.fox2code.flexver.components.PreReleaseVersionComponent;
import com.fox2code.flexver.components.VersionComponent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Internal implementation class for FlexVer
 */
final class FlexVerParserImpl {
    private FlexVerParserImpl() {}

    static final VersionComponent[] NULL_VERSION_COMPONENT_ARRAY = new VersionComponent[0];

    public static VersionComponent[] decompose(String str) {
        return decomposeImpl(str, str.length());
    }

    /*
     * Break apart a string into intuitive version components, by splitting it where a run of
     * characters changes from numeric to non-numeric.
     */
    // @VisibleForTesting
    static VersionComponent[] decomposeImpl(String str, int length) {
        if (length == 0) return FlexVerParserImpl.NULL_VERSION_COMPONENT_ARRAY;
        if (str.length() < length) throw new IndexOutOfBoundsException("length is higher than string length");
        boolean lastWasNumber = isAsciiDigit(str.codePointAt(0));
        int totalCodepoints = str.codePointCount(0, length);
        int[] accum = new int[totalCodepoints];
        ArrayList<VersionComponent> out = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < length; i++) {
            int cp = str.codePointAt(i);
            if (Character.charCount(cp) == 2) i++;
            if (cp == '+') break; // remove appendices
            boolean number = isAsciiDigit(cp);
            if (number != lastWasNumber || (cp == '-' && j > 0 && accum[0] != '-')) {
                out.add(createComponent(lastWasNumber, accum, j));
                j = 0;
                lastWasNumber = number;
            }
            accum[j] = cp;
            j++;
        }
        out.add(createComponent(lastWasNumber, accum, j));
        return out.toArray(FlexVerParserImpl.NULL_VERSION_COMPONENT_ARRAY);
    }

    private static boolean isAsciiDigit(int cp) {
        return cp >= '0' && cp <= '9';
    }

    static String recompose(VersionComponent[] versionComponents) {
        if (versionComponents.length == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < versionComponents.length; i++) {
            versionComponents[i].recompose(stringBuilder, i != 0);
        }
        return stringBuilder.toString();
    }

    private static VersionComponent createComponent(boolean number, int[] s, int j) {
        s = Arrays.copyOfRange(s, 0, j);
        if (number) {
            return NumericVersionComponent.make(s);
        } else if (s.length > 1 && s[0] == '-') {
            return PreReleaseVersionComponent.make(s);
        } else {
            return VersionComponent.make(s);
        }
    }

    static int compare(VersionComponent[] a, VersionComponent[] b) {
        int maxIteration = Math.max(a.length, b.length);
        for (int i = 0; i < maxIteration; i++) {
            int c = get(a, i).compareTo(get(b, i));
            if (c != 0) return c;
        }
        return 0;
    }

    static boolean flexVerMatch(VersionComponent[] a, VersionComponent[] b) {
        int maxIteration = b.length;
        for (int i = 0; i < maxIteration; i++) {
            int c = get(a, i).compareTo(get(b, i));
            if (c != 0) return false;
        }
        return true;
    }

    private static VersionComponent get(VersionComponent[] li, int i) {
        return i >= li.length ? VersionComponent.NULL : li[i];
    }
}
