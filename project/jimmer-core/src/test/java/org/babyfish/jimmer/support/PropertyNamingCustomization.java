package org.babyfish.jimmer.support;

import org.babyfish.jimmer.jackson.codec.JsonCodecCustomization;

public class PropertyNamingCustomization implements JsonCodecCustomization {
    private final PropertyNaming propertyNaming;

    public PropertyNamingCustomization(PropertyNaming propertyNaming) {
        this.propertyNaming = propertyNaming;
    }

    @Override
    public void customizeV2(com.fasterxml.jackson.databind.json.JsonMapper.Builder builder) {
        builder.propertyNamingStrategy(propertyNaming.strategyV2);
    }

    @Override
    public void customizeV3(tools.jackson.databind.json.JsonMapper.Builder builder) {
        builder.propertyNamingStrategy(propertyNaming.strategyV3);
    }

    public enum PropertyNaming {
        LOWER_CAMEL_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE,
                tools.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE
        ),
        UPPER_CAMEL_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE,
                tools.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE
        ),
        LOWER_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CASE,
                tools.jackson.databind.PropertyNamingStrategies.LOWER_CASE
        ),
        SNAKE_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE,
                tools.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
        ),
        KEBAB_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.KEBAB_CASE,
                tools.jackson.databind.PropertyNamingStrategies.KEBAB_CASE
        ),
        LOWER_DOT_CASE(
                com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_DOT_CASE,
                tools.jackson.databind.PropertyNamingStrategies.LOWER_DOT_CASE
        );

        private final com.fasterxml.jackson.databind.PropertyNamingStrategy strategyV2;
        private final tools.jackson.databind.PropertyNamingStrategy strategyV3;

        PropertyNaming(com.fasterxml.jackson.databind.PropertyNamingStrategy strategyV2,
                       tools.jackson.databind.PropertyNamingStrategy strategyV3) {
            this.strategyV2 = strategyV2;
            this.strategyV3 = strategyV3;
        }
    }
}
