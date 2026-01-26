package org.babyfish.jimmer.sql.event.binlog.impl;

import tools.jackson.databind.module.SimpleModule;

class BinLogModule3 extends SimpleModule {

    private final BinLogParser parser;

    BinLogModule3(BinLogParser parser) {
        this.parser = parser;
    }

    @Override
    public void setupModule(SetupContext ctx) {
        super.setupModule(ctx);
        ctx.addDeserializers(new BinLogDeserializers3(parser));
    }
}
