package com.svydovets.bibirnate.annotation;

import static com.svydovets.bibirnate.session.query.FetchType.EAGER;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;


/**
 * Defines single value association to another entity that has may-to-one multiplicity.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {

    FetchType fetch() default EAGER;

    CascadeType[] cascade() default {};
}
