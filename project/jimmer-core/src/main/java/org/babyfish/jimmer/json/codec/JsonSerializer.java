package org.babyfish.jimmer.json.codec;

public interface JsonSerializer {
    String serialize(Object obj) throws JsonDeserializationException;
}