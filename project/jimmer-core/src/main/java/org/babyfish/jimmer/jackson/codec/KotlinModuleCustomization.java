package org.babyfish.jimmer.jackson.codec;

import static org.babyfish.jimmer.jackson.ClassUtils.classExists;

public class KotlinModuleCustomization implements JsonCodecCustomization{
    @Override
    public void customizeV2(com.fasterxml.jackson.databind.json.JsonMapper.Builder builder) {
        if (classExists("com.fasterxml.jackson.module.kotlin.KotlinModule")) {
            builder.addModule(new com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build());
        }
    }

    @Override
    public void customizeV3(tools.jackson.databind.json.JsonMapper.Builder builder) {
        if (classExists("tools.jackson.module.kotlin.KotlinModule")) {
            builder.addModule(new tools.jackson.module.kotlin.KotlinModule.Builder().build());
        }
    }
}
