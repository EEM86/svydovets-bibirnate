package com.svydovets.bibirnate.entities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "test_table")
@Data
public class EntityPrimitives {
    @Id
    private long id;

    @Column(name = "int_field")
    private int intField;

    @Column(name = "short_field")
    private short shortField;

    private float float_field;

    @Column(name = "double_field")
    private double doubleField;

    private boolean boolean_field;

    @Column(name = "char_field")
    private char charField;

    @Column(name = "big_decimal_field")
    private BigDecimal bigDecimalField;

    @Column(name = "big_integer_field")
    private BigInteger bigIntegerField;

    @Column(name = "string_field")
    private String stringField;

    private Date date_field;

    @Column(name = "timestamp_field")
    private Timestamp timestampField;

    @Column(name = "local_date_field")
    private LocalDate localDateField;

    @Column(name = "local_date_time_field")
    private LocalDateTime localDateTimeField;

    @Column(name = "blob_field")
    private byte[] blobField;
}
