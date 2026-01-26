package org.babyfish.jimmer.json.codec;

public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(Throwable cause) {
        super(cause);
    }

    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
