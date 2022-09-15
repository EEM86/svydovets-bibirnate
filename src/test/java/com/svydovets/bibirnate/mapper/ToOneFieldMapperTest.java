package com.svydovets.bibirnate.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.entity.ManyToOneRelatedEntity;
import com.svydovets.bibirnate.entity.OneToOneRelatedEntity;
import com.svydovets.bibirnate.entity.TestEntityWithToOneRelations;
import com.svydovets.bibirnate.exceptions.EntityMappingException;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;

class ToOneFieldMapperTest {

    private TestEntityWithToOneRelations entityWithMappedFields;

    private EntityFieldMapper toOneFieldMapper;

    @BeforeEach
    void createEntityWithMappedFields() {
        entityWithMappedFields = new TestEntityWithToOneRelations();
        entityWithMappedFields.setId(1L);
    }


    @Test
    void toOneFieldMapperShouldThrowEntityMappingExceptionIfJoinColumnIsNotSpecifiedInOneToOne()
        throws NoSuchFieldException {
        JdbcEntityDao jdbcEntityDao = mock(BaseJdbcEntityDao.class);
        toOneFieldMapper = new ToOneFieldMapper(jdbcEntityDao);

        Field toOneFieldWithoutJoinColumn =
          entityWithMappedFields.getClass().getDeclaredField("oneToOneRelatedEntityWithoutJoinColumn");

        assertThrows(EntityMappingException.class,
          () -> toOneFieldMapper.mapField(toOneFieldWithoutJoinColumn, entityWithMappedFields, 1));
    }

    @Test
    void toOneFieldMapperThrowsEntityMappingExceptionIfJoinColumnIsNotSpecifiedInManyToOne()
        throws NoSuchFieldException {
        JdbcEntityDao jdbcEntityDao = mock(BaseJdbcEntityDao.class);
        toOneFieldMapper = new ToOneFieldMapper(jdbcEntityDao);

        Field toOneFieldWithoutJoinColumn =
          entityWithMappedFields.getClass().getDeclaredField("manyToOneRelatedEntityWithoutJoinColumn");

        assertThrows(EntityMappingException.class,
          () -> toOneFieldMapper.mapField(toOneFieldWithoutJoinColumn, entityWithMappedFields, 1));
    }

    @Test
    void toOneFieldMapperMapsFieldWithOneToOne() throws NoSuchFieldException {
        var oneToOneRelatedEntity = new OneToOneRelatedEntity();
        oneToOneRelatedEntity.setId(3L);

        JdbcEntityDao jdbcEntityDao = mock(BaseJdbcEntityDao.class);
        when(jdbcEntityDao.findById(3L, OneToOneRelatedEntity.class)).thenReturn(Optional.of(oneToOneRelatedEntity));

        toOneFieldMapper = new ToOneFieldMapper(jdbcEntityDao);

        Field toOneFieldWithoutJoinColumn =
          entityWithMappedFields.getClass().getDeclaredField("oneToOneRelatedEntity");

        toOneFieldMapper.mapField(toOneFieldWithoutJoinColumn, entityWithMappedFields, 3L);

        assertEquals(oneToOneRelatedEntity, entityWithMappedFields.getOneToOneRelatedEntity());
    }

    @Test
    void toOneFieldMapperMapsFieldWithManyToOne() throws NoSuchFieldException {
        var manyToOneRelatedEntity = new ManyToOneRelatedEntity();
        manyToOneRelatedEntity.setId(5L);

        JdbcEntityDao jdbcEntityDao = mock(JdbcEntityDao.class);
        when(jdbcEntityDao.findById(5L, ManyToOneRelatedEntity.class)).thenReturn(Optional.of(manyToOneRelatedEntity));

        toOneFieldMapper = new ToOneFieldMapper(jdbcEntityDao);

        Field toOneFieldWithoutJoinColumn =
          entityWithMappedFields.getClass().getDeclaredField("manyToOneRelatedEntity");

        toOneFieldMapper.mapField(toOneFieldWithoutJoinColumn, entityWithMappedFields, 5L);

        assertEquals(manyToOneRelatedEntity, entityWithMappedFields.getManyToOneRelatedEntity());
    }


}
