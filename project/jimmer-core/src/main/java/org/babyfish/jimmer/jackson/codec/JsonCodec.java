package org.babyfish.jimmer.jackson.codec;

import org.babyfish.jimmer.jackson.ClassUtils;
import org.babyfish.jimmer.jackson.v2.JsonCodecImpl;
import org.babyfish.jimmer.jackson.v3.JsonCodecImpl3;

import java.util.List;
import java.util.Map;

public interface JsonCodec<JT> {
    JsonCodec<JT> withCustomizations(JsonCodecCustomization... customizations);

    JsonConverter converter();

    <T> JsonReader<T> readerFor(Class<T> clazz);

    <T> JsonReader<T> readerFor(TypeCreator<JT> typeCreator);

    <T> JsonReader<T[]> readerForArrayOf(Class<T> componentType);

    <T> JsonReader<List<T>> readerForListOf(Class<T> elementType);

    <V> JsonReader<Map<String, V>> readerForMapOf(Class<V> valueType);

    default <K, V> JsonReader<Map<K, V>> readerForMapOf(Class<K> keyType, Class<V> valueType) {
        return readerFor(tf -> tf.constructMapType(keyType, valueType));
    }

    JsonReader<Node> treeReader();

    JsonWriter writer();

    JsonWriter writerFor(Class<?> clazz);

    JsonWriter writerFor(TypeCreator<JT> typeCreator);

    class Detector {
        private static final JsonCodec<?> JSON_CODEC;
        private static final JsonCodec<?> JSON_CODEC_WITHOUT_IMMUTABLE_MODULE;

        public static JsonCodec<?> jsonCodec() {
            return JSON_CODEC;
        }

        public static JsonCodec<?> jsonCodecWithoutImmutableModule() {
            return JSON_CODEC_WITHOUT_IMMUTABLE_MODULE;
        }

        static {
            if (ClassUtils.classExists("tools.jackson.databind.ObjectMapper")) {
                JSON_CODEC_WITHOUT_IMMUTABLE_MODULE = new JsonCodecImpl3();
            } else if (ClassUtils.classExists("com.fasterxml.jackson.databind.ObjectMapper")) {
                JSON_CODEC_WITHOUT_IMMUTABLE_MODULE = new JsonCodecImpl();
            } else {
                throw new IllegalStateException("Jackson is required");
            }
            JSON_CODEC = JSON_CODEC_WITHOUT_IMMUTABLE_MODULE.withCustomizations(new ImmutableModuleCustomization());
        }
    }
}
