package com.svydovets.bibirnate.session.impl;

import static com.svydovets.bibirnate.cache.key.factory.KeyParamFactory.generateKeyParam;
import static com.svydovets.bibirnate.session.impl.JdbcEntityDaoFactory.createJdbcEntityDao;

import java.util.Optional;
import javax.sql.DataSource;

import com.svydovets.bibirnate.cache.Cache;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.key.factory.KeyParamFactory;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.session.Session;

public class SessionImpl implements Session {
    private final JdbcEntityDao jdbcEntityDao;
    private final Cache cache;
    private final Cache secondLevelCache;

    public SessionImpl(DataSource dataSource, Cache secondLevelCache) {
        this.jdbcEntityDao = createJdbcEntityDao(dataSource);
        cache = new Cache(50);// todo: change that it will be configurable
        this.secondLevelCache = secondLevelCache;
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> type) {
        // generation of the EntityKeyParam
        AbstractKeyParam<T> keyParam = generateKeyParam(type, id);
        // attempt of the extraction from the cache
        Optional<T> result = (Optional<T>)cache.get(keyParam, EntityKeyExtractorCommand.class);
        // check from the first level cache
        if (result.isEmpty()){
            //check from the second level cache
            if (SessionFactoryImpl.isSecondLevelCacheEnabled()){
                result = (Optional<T>)secondLevelCache.get(keyParam, EntityKeyExtractorCommand.class);
                if (result.isEmpty()) {
                    //if cache is empty then we go to DB
                    result = jdbcEntityDao.findById(id, type);
                    // put result to the cache
                    cache.put(keyParam, result);
                    secondLevelCache.put(keyParam, result);
                }
            } else {
                //if cache is empty then we go to DB
                result = jdbcEntityDao.findById(id, type);
                // put result to the cache
                cache.put(keyParam, result);
            }
        }
        return result;
    }
}
