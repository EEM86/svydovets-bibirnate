package com.svydovets.bibirnate.session.query.processor;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parent {

    private String columnName;

    private Object id;

    private Field field;
}
