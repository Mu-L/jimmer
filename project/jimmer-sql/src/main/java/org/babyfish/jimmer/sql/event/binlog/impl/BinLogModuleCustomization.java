package org.babyfish.jimmer.sql.event.binlog.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.babyfish.jimmer.jackson.codec.JsonCodecCustomization;

public class BinLogModuleCustomization implements JsonCodecCustomization {
    private final BinLogParser parser;

    public BinLogModuleCustomization(BinLogParser parser) {
        this.parser = parser;
    }

    @Override
    public void customizeV2(JsonMapper.Builder builder) {
        builder.addModule(new BinLogModule(parser));
    }

    @Override
    public void customizeV3(tools.jackson.databind.json.JsonMapper.Builder builder) {
        builder.addModule(new BinLogModule3(parser));
    }
}
