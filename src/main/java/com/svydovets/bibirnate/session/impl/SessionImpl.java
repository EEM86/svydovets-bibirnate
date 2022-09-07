package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.jdbc.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.util.Optional;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.CacheContainer;
import com.svydovets.bibirnate.cache.CacheUtils;
import com.svydovets.bibirnate.jdbc.JdbcEntityDao;
import com.svydovets.bibirnate.session.Session;

public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final CacheContainer cacheContainer;

    public SessionImpl(DataSource dataSource, CacheContainer cacheContainer) {
        this.jdbcEntityDao = createJdbcEntityDao(dataSource); // toDo should we create session without factory?
        this.cacheContainer = cacheContainer;
    }

    @Override
    public <T> T findById(Object id, Class<T> type) {
        Optional<T> result = CacheUtils.extract(cacheContainer, type, id);

        if (result.isEmpty()) {
            result = jdbcEntityDao.findById(id, type);
            CacheUtils.put(cacheContainer, type, id, result);
        }

        return result.orElse(null);
    }
}
