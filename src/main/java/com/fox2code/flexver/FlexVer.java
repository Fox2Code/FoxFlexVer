package com.fox2code.flexver;

import com.fox2code.flexver.components.VersionComponent;

public final class FlexVer extends FlexVerPredicate implements Comparable<FlexVer> {
    private static final FlexVer NULL = new FlexVer(FlexVerParserImpl.NULL_VERSION_COMPONENT_ARRAY);

    private FlexVer(VersionComponent[] versionComponents) {
        super(FlexVerPredicateType.EQUAL, versionComponents);
    }

    @Override
    public int compareTo(FlexVer other) {
        return FlexVerParserImpl.compare(this.versionComponents, other.versionComponents);
    }

    public boolean isGreater(String other) {
        return FlexVerParserImpl.compare(this.versionComponents, FlexVerParserImpl.decompose(other)) > 0;
    }

    public boolean isGreater(FlexVer other) {
        return FlexVerParserImpl.compare(this.versionComponents, other.versionComponents) > 0;
    }

    public boolean isGreaterOrEqual(String other) {
        return FlexVerParserImpl.compare(this.versionComponents, FlexVerParserImpl.decompose(other)) >= 0;
    }

    public boolean isGreaterOrEqual(FlexVer other) {
        return FlexVerParserImpl.compare(this.versionComponents, other.versionComponents) >= 0;
    }

    @Override
    public String toString() {
        return FlexVerParserImpl.recompose(this.versionComponents);
    }

    public static FlexVer parse(String flexVer) {
        if (flexVer.isEmpty() || flexVer.startsWith("+")) return NULL;
        return new FlexVer(FlexVerParserImpl.decompose(flexVer));
    }
}
