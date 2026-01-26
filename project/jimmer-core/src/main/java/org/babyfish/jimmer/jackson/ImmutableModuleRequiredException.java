package org.babyfish.jimmer.jackson;

import org.babyfish.jimmer.jackson.v2.ImmutableModule;

public class ImmutableModuleRequiredException extends RuntimeException {

    public ImmutableModuleRequiredException() {
        super(
                "Immutable object cannot be serialized by ordinary ObjectMapper, " +
                        "please register the \"" +
                        ImmutableModule.class +
                        "\" into the ObjectMapper"
        );
    }
}
