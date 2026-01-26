package org.babyfish.jimmer.jackson.codec;

import org.babyfish.jimmer.jackson.v2.ImmutableModule;
import org.babyfish.jimmer.jackson.v3.ImmutableModule3;

public class ImmutableModuleCustomization implements JsonCodecCustomization {
    @Override
    public void customizeV2(com.fasterxml.jackson.databind.json.JsonMapper.Builder builder) {
        builder.addModule(new ImmutableModule());
    }

    @Override
    public void customizeV3(tools.jackson.databind.json.JsonMapper.Builder builder) {
        builder.addModule(new ImmutableModule3());
    }
}
