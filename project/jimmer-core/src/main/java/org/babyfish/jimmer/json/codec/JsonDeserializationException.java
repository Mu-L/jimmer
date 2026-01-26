package org.babyfish.jimmer.json.codec;

public class JsonDeserializationException extends RuntimeException {
    public JsonDeserializationException(Throwable cause) {
        super(cause);
    }

    public JsonDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
