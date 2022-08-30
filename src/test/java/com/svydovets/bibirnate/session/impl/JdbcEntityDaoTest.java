package com.svydovets.bibirnate.session.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.mapper.EntityMapperService;

import lombok.SneakyThrows;

class JdbcEntityDaoTest {

    private static final long ID = 13L;

    @Test
    @SneakyThrows
    void findById_shouldCallFindBy() {
        var idField = EntityPrimitives.class.getDeclaredField("id");
        var mock = mock(JdbcEntityDao.class);

        var foundEntity = Optional.of( new EntityPrimitives());
        when(mock.findBy(idField, ID, EntityPrimitives.class)).thenReturn(foundEntity);
        when(mock.findById(ID, EntityPrimitives.class)).thenCallRealMethod();

        assertSame(foundEntity, mock.findById(ID, EntityPrimitives.class));
    }

    @Test
    @SneakyThrows
    void findBy_shouldReturnEmptyOptional_ifNothingFound() {
        var dataSource = mock(DataSource.class);
        var connection = mock(Connection.class);
        var statement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);

        when(statement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement("select * from test_table where id = ?")).thenReturn(statement);
        when(dataSource.getConnection()).thenReturn(connection);
        when(resultSet.next()).thenReturn(false);

        var jdbcEntityDao = new JdbcEntityDao(dataSource);
        var idField = EntityPrimitives.class.getDeclaredField("id");

        assertEquals(Optional.empty(), jdbcEntityDao.findBy(idField, ID, EntityPrimitives.class));

        verify(statement).setObject(1, ID);
    }

    @Test
    @SneakyThrows
    void findBy_shouldReturnEntity() {
        var dataSource = mock(DataSource.class);
        var connection = mock(Connection.class);
        var statement = mock(PreparedStatement.class);
        var resultSet = mock(ResultSet.class);
        var entity = new EntityPrimitives();

        when(statement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement("select * from test_table where id = ?")).thenReturn(statement);
        when(dataSource.getConnection()).thenReturn(connection);
        when(resultSet.next()).thenReturn(true);

        var jdbcEntityDao = new JdbcEntityDao(dataSource);
        var idField = EntityPrimitives.class.getDeclaredField("id");

        try (var entityMapperService = mockStatic(EntityMapperService.class)) {
            entityMapperService.when(() -> EntityMapperService.mapToObject(EntityPrimitives.class, resultSet))
              .thenReturn(entity);

            assertEquals(Optional.of(entity), jdbcEntityDao.findBy(idField, ID, EntityPrimitives.class));
        }

        verify(statement).setObject(1, ID);
    }
}