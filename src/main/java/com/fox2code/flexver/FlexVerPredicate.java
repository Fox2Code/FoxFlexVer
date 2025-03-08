package com.fox2code.flexver;

import com.fox2code.flexver.components.VersionComponent;

import java.util.Arrays;
import java.util.Objects;

public class FlexVerPredicate {
    public static final FlexVerPredicate ANY = new FlexVerPredicate(
            FlexVerPredicateType.MATCH, FlexVerParserImpl.NULL_VERSION_COMPONENT_ARRAY);
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
        return this.flexVerPredicateType.recompose(this.versionComponents);
    }

    /**
     *
     * @param flexVerPredicate predicate to parse
     * @return the compiled predicate, may return a {@link FlexVer} if the input string is a regular predicate.
     */
    public static FlexVerPredicate parse(String flexVerPredicate) {
        if ("*".equals(flexVerPredicate)) {
            return ANY;
        }
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
        VersionComponent[] versionComponents =
                FlexVerParserImpl.decomposeImpl(flexVerPredicate, lengthToParse);
        if (predicateType == FlexVerPredicateType.MATCH && versionComponents.length == 0) {
            return ANY;
        }
        return new FlexVerPredicate(predicateType, versionComponents);
    }

    public enum FlexVerPredicateType {
        GREATER_EQUAL(">=") {
            @Override
            boolean match(VersionComponent[] version, VersionComponent[] check) {
                return FlexVerParserImpl.compare(version, check) >= 0;
            }
        },
        LESSER_EQUAL("<=") {
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
        LESSER("<") {
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

            @Override
            String recompose(VersionComponent[] check) {
                return check.length == 0 ? "*" : super.recompose(check);
            }
        };

        private static final FlexVerPredicateType[] VALUES = FlexVerPredicateType.values();
        public final String sign;
        public final boolean internal;

        FlexVerPredicateType(String sign) {
            this.sign = sign;
            this.internal = sign.startsWith(".");
        }

        abstract boolean match(VersionComponent[] version, VersionComponent[] check);

        String recompose(VersionComponent[] check) {
            return FlexVerParserImpl.recompose(check) + this.sign;
        }
    }
}
