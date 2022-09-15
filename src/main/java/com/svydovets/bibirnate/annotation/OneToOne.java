package com.svydovets.bibirnate.annotation;

import static com.svydovets.bibirnate.session.query.FetchType.EAGER;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;

/**
 * Defines single value association to another entity that has one-to-one multiplicity
 * for now works for selection only with eager fetch type
 * (related entity will be loaded at the same time when entity that owns this relation is loaded)
 * and on the side that owns foreign key (must be specified in JoinColumn annotation).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToOne {

    FetchType fetch() default EAGER;

    CascadeType[] cascade() default {};

    String mappedBy() default "";
}
