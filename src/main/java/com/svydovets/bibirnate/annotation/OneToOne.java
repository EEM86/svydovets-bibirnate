package com.svydovets.bibirnate.annotation;

import static com.svydovets.bibirnate.session.query.FetchType.EAGER;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.svydovets.bibirnate.session.query.CascadeType;
import com.svydovets.bibirnate.session.query.FetchType;

//todo: add javadoc in scope of BIB-15
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToOne {

    FetchType fetch() default EAGER;

    CascadeType[] cascade() default {};
}
