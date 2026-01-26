package org.babyfish.jimmer.json.codec;

public interface JsonDeserializer {
    <T> T deserialize(String json, Class<T> type) throws JsonDeserializationException;
}