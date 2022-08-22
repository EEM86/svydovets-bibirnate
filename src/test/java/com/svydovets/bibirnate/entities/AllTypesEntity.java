package com.svydovets.bibirnate.entities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@com.svydovets.bibirnate.annotation.Entity
@Table(name = "test_table")
@Data
public class AllTypesEntity {

    @Id
    private Long id;

    @Column(name = "int_field")
    private Integer intField;

    @Column(name = "short_field")
    private Short shortField;

    @Column(name = "float_field")
    private Float floatField;

    @Column(name = "double_field")
    private Double doubleField;

    @Column(name = "boolean_field")
    private Boolean booleanField;

    @Column(name = "char_field")
    private Character charField;

    @Column(name = "big_decimal_field")
    private BigDecimal bigDecimalField;

    @Column(name = "big_integer_field")
    private BigInteger bigIntegerField;

    @Column(name = "string_field")
    private String stringField;

    @Column(name = "date_field")
    private Date dateField;

    @Column(name = "timestamp_field")
    private Timestamp timestampField;

    @Column(name = "local_date_field")
    private LocalDate localDateField;

    @Column(name = "local_date_time_field")
    private LocalDateTime localDateTimeField;

    @Column(name = "blob_field")
    private byte[] blobField;
}
