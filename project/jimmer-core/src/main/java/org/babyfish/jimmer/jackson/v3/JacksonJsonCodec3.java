package org.babyfish.jimmer.jackson.v3;

import org.babyfish.jimmer.json.codec.JsonCodec;
import org.babyfish.jimmer.json.codec.JsonDeserializationException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JacksonJsonCodec3 implements JsonCodec {
    private final ObjectMapper mapper;

    public JacksonJsonCodec3(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonCodec3() {
        this(createDefaultMapper());
    }

    private static ObjectMapper createDefaultMapper() {
        return JsonMapper.builder()
                .addModule(new ImmutableModule3())
                .build();
    }

    @Override
    public <T> T deserialize(String json, Class<T> type) throws JsonDeserializationException {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonDeserializationException(e);
        }
    }

    @Override
    public String serialize(Object obj) throws JsonDeserializationException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonDeserializationException(e);
        }
    }
}
