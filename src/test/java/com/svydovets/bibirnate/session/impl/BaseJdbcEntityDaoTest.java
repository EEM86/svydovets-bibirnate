package com.svydovets.bibirnate.session.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.svydovets.bibirnate.entities.EntityPrimitives;
import com.svydovets.bibirnate.entities.PersonSimpleEntity;
import com.svydovets.bibirnate.mapper.EntityMapperService;

import lombok.SneakyThrows;

class BaseJdbcEntityDaoTest {

    private static final long ID = 13L;

    @Test
    @SneakyThrows
    void findById_shouldCallFindBy() {
        var idField = EntityPrimitives.class.getDeclaredField("id");
        var mock = mock(BaseJdbcEntityDao.class);

        var foundEntity = Optional.of(new EntityPrimitives());
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

        var jdbcEntityDao = new BaseJdbcEntityDao(connection);
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

        var jdbcEntityDao = new BaseJdbcEntityDao(connection);
        var idField = EntityPrimitives.class.getDeclaredField("id");

        try (var entityMapperService = mockStatic(EntityMapperService.class)) {
            entityMapperService.when(() -> EntityMapperService.mapToObject(EntityPrimitives.class, resultSet))
              .thenReturn(entity);

            assertEquals(Optional.of(entity), jdbcEntityDao.findBy(idField, ID, EntityPrimitives.class));
        }

        verify(statement).setObject(1, ID);
    }

    @SneakyThrows
    @Test
    void remove() {
        var connectionMock = mock(Connection.class);
        var statement = mock(Statement.class);
        var dao = new JdbcEntityDao(connectionMock);
        var person = new PersonSimpleEntity(1L, "name", "last_name", "blindValue");
        when(connectionMock.createStatement()).thenReturn(statement);
        dao.remove(person);

        verify(connectionMock).createStatement();
        verify(statement).execute(anyString());
    }

}