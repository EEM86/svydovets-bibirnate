package com.svydovets.bibirnate.mapper.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import com.svydovets.bibirnate.entities.AllTypesEntity;
import com.svydovets.bibirnate.utils.CommonConstants;

class BlobConverterTest {

    @Test
    void convert() throws NoSuchFieldException, SQLException {
        var converter = new BlobConverter();
        Blob myBlob = new SerialBlob(CommonConstants.BYTE_ARRAY_CONSTANT);
        var res = converter.convert(myBlob,
          AllTypesEntity.class.getDeclaredField("blobField"));
        assertEquals(byte.class, res.getClass().getComponentType());
    }

    private static Stream<Arguments> provideEntityClassesWithExceptionMessages() {
        return Stream.of(
          Arguments.of("bigIntegerField", BigInteger.class),
          Arguments.of("bigDecimalField", BigDecimal.class)
        );
    }
}