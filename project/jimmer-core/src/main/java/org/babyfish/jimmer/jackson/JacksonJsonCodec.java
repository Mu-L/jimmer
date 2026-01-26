package org.babyfish.jimmer.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.babyfish.jimmer.json.codec.JsonCodec;
import org.babyfish.jimmer.json.codec.JsonDeserializationException;

public class JacksonJsonCodec implements JsonCodec {
    private final ObjectMapper mapper;

    public JacksonJsonCodec(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonCodec() {
        this(createDefaultMapper());
    }

    private static ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ImmutableModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
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
