package com.svydovets.bibirnate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies whether an entity should be cached in second level cache if caching is enabled.
 * Cacheable(false) means that the entity and its state must not be cached by the provider.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * (Optional) Whether the entity should be cached.
     */
    boolean value() default true;
}
