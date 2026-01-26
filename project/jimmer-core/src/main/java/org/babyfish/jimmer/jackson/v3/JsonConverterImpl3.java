package org.babyfish.jimmer.jackson.v3;

import org.babyfish.jimmer.jackson.codec.JsonConverter;
import org.babyfish.jimmer.jackson.codec.JsonTypeFactory;
import org.babyfish.jimmer.jackson.codec.TypeCreator;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

public class JsonConverterImpl3 implements JsonConverter {
    private final ObjectMapper mapper;
    private final JsonTypeFactory<JavaType> typeFactory;

    public JsonConverterImpl3(ObjectMapper mapper) {
        this.mapper = mapper;
        this.typeFactory = new JsonTypeFactoryImpl3(mapper.getTypeFactory());
    }

    @Override
    public <T> T convert(Object value, Class<T> targetType) throws Exception {
        return mapper.convertValue(value, targetType);
    }

    @Override
    public <T> T convert(Object value, TypeCreator typeCreator) throws Exception {
        return mapper.convertValue(value, (JavaType) typeCreator.createType(typeFactory));
    }
}
