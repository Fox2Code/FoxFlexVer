package com.fox2code.flexver;

import com.fox2code.flexver.components.VersionComponent;

import java.util.Arrays;
import java.util.Objects;

public class FlexVerPredicate {
    private final FlexVerPredicateType flexVerPredicateType;
    final VersionComponent[] versionComponents;

    FlexVerPredicate(FlexVerPredicateType flexVerPredicateType, VersionComponent[] versionComponents) {
        this.flexVerPredicateType = flexVerPredicateType;
        this.versionComponents = versionComponents;
    }

    public final boolean match(String flexVer) {
        return this.flexVerPredicateType.match(FlexVerParserImpl.decompose(flexVer), this.versionComponents);
    }

    public final boolean match(FlexVer flexVer) {
        return this.flexVerPredicateType.match(flexVer.versionComponents, this.versionComponents);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlexVerPredicate that = (FlexVerPredicate) o;
        return flexVerPredicateType == that.flexVerPredicateType &&
                Objects.deepEquals(versionComponents, that.versionComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flexVerPredicateType, Arrays.hashCode(versionComponents));
    }

    @Override
    public String toString() {
        return FlexVerParserImpl.recompose(this.versionComponents) + this.flexVerPredicateType.sign;
    }

    /**
     *
     * @param flexVerPredicate predicate to parse
     * @return the compiled predicate, may return a {@link FlexVer} if the input string is a regular predicate.
     */
    public static FlexVerPredicate parse(String flexVerPredicate) {
        int lengthToParse = flexVerPredicate.length();
        FlexVerPredicateType predicateType = FlexVerPredicateType.EQUAL;
        for (FlexVerPredicateType type : FlexVerPredicateType.VALUES) {
            if (flexVerPredicate.endsWith(type.sign)) {
                if (!(type.internal && flexVerPredicate.contains("+"))) {
                    lengthToParse -= type.sign.length();
                    predicateType = type;
                }
                break;
            }
        }
        if (lengthToParse == flexVerPredicate.length()) {
            return FlexVer.parse(flexVerPredicate);
        }
        return new FlexVerPredicate(predicateType,
                FlexVerParserImpl.decomposeImpl(flexVerPredicate, lengthToParse));
    }

    public enum FlexVerPredicateType {
        GREATER_EQUAL(">=") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) >= 0;
            }
        },
        LESS_EQUAL("<=") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) <= 0;
            }
        },
        GREATER(">") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) > 0;
            }
        },
        LESS("<") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) < 0;
            }
        }, UNEQUAL("!=") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) != 0;
            }
        }, EQUAL("=") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) == 0;
            }
        }, MATCH(".*") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.flexVerMatch(version, check);
            }
        };

        private static final FlexVerPredicateType[] VALUES = FlexVerPredicateType.values();
        public final String sign;
        public final boolean internal;

        FlexVerPredicateType(String sign) {
            this.sign = sign;
            this.internal = sign.startsWith(".");
        }

        abstract boolean match(VersionComponent[] self, VersionComponent[] other);
    }
}
