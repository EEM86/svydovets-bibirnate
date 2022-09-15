package com.svydovets.bibirnate.session.query;

import static com.svydovets.bibirnate.SessionTestUtil.mockConnectionMetadata;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.entity.BiberEntity;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.jdbc.impl.BaseJdbcEntityDao;

public class TypedQueryTest {

    private static Connection connection;

    private static JdbcEntityDao jdbcEntityDao;
    private static String queryString;
    private static List<Object> parameters;
    private static Class<?> entityType;
    private static Cache secondLevelCache;
    private static CacheContainer cacheContainer;
    private static Query query;

    @BeforeAll
    static void beforeAll() throws SQLException {
        connection = mock(Connection.class);
        jdbcEntityDao = new BaseJdbcEntityDao(connection);
        cacheContainer = mock(CacheContainer.class);
        mockConnectionMetadata(connection);
        queryString = "select * from table";
        entityType = BiberEntity.class;
        secondLevelCache = new Cache(200_000);
        query = new TypedQuery(jdbcEntityDao, queryString, entityType, cacheContainer);
    }

//    @Test
//    @SneakyThrows
//    void getSingleResult_throwsNoResultException() {
//        when(connection.prepareStatement(queryString)).thenReturn(mock(PreparedStatement.class));
//        when(query.getSingleResult()).thenThrow(NoResultException.class);
//        assertThrows(NoResultException.class, () -> query.getSingleResult());
//    }

}
