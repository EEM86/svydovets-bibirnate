package com.svydovets.bibirnate.cache;


import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.QueryKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.exception.CacheOverloadException;

/**
 * This class is LRU cache realization.
 * It provides
 */
public class Cache {

    private static final int DEFAULT_CACHE_SIZE = 10_000;
    private static final int MIN_CACHE_SIZE = 20;
    private static final int DEFAULT_MAX_CACHE_SIZE = 50_000;
    private static final int ZERO_DAYS = 0;
    private static final int ONE_DAY = 1;
    private static final int ONE_HOUR = 1;
    private static final int TWO_HOURS = 2;
    private static final int ONE_HUNDRED = 100;
    private static final int MAX_CACHE_SIZE_PERCENTAGE = 80;
    private final int cacheSize;
    private final int maxCacheSize;
    private final Map<Key<?>, Object> cacheMap;
    private Map<Class<? extends KeyExtractorCommand>, KeyExtractorCommand> keyExtractorCommandMap;
    private Map<Class<? extends InvalidationCommand>, InvalidationCommand> invalidationCommandMap;

    private LocalDateTime lastClean;

    public Cache() {
        this(DEFAULT_CACHE_SIZE);
    }

    public Cache(int cacheSize) {
        this(cacheSize, DEFAULT_MAX_CACHE_SIZE);
    }

    public Cache(int cacheSize, int maxCacheSize) {
        if ((maxCacheSize < MIN_CACHE_SIZE * 2) || cacheSize < MIN_CACHE_SIZE || cacheSize > maxCacheSize) {
            throw new IllegalArgumentException(
              String.format("Inappropriate [cacheSize]. [cacheSize] cannot be less then %s or more than %s",
                maxCacheSize, maxCacheSize));
        }

        this.cacheSize = cacheSize;
        this.maxCacheSize = maxCacheSize;
        this.lastClean = LocalDateTime.now();
        this.cacheMap = new ConcurrentHashMap<>(this.cacheSize);

        initializeKeyExtractorCommandMap();
        initializeInvalidationCommandMap();
    }

    public void put(AbstractKeyParam<?> keyParam, Object value) {
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));

        cacheMap.put(new Key<>(keyParam), value);

        autoClean();
    }

    public Optional<Object> get(AbstractKeyParam<?> keyParam, Class<? extends KeyExtractorCommand> commandType) {
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));
        Objects.requireNonNull(commandType, String.format(PARAMETER_CANNOT_BE_NULL, commandType));

        Optional<Key<?>> key = getKey(keyParam, commandType);

        return key.map(cacheMap::get);
    }

    public void invalidateRelated(AbstractKeyParam<?> keyParam, Class<? extends KeyExtractorCommand> extractCommandType,
                                  Class<? extends InvalidationCommand> invalidationCommandType) {
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));
        Objects.requireNonNull(extractCommandType, String.format(PARAMETER_CANNOT_BE_NULL, extractCommandType));
        Objects.requireNonNull(invalidationCommandType,
          String.format(PARAMETER_CANNOT_BE_NULL, invalidationCommandType));


        getKey(keyParam, extractCommandType).ifPresent(
          value -> invalidationCommandMap.get(invalidationCommandType).executeInvalidate(cacheMap, value));
    }

    /**
     * Makes clear for {@link Cache#cacheMap}.
     */
    public void clear() {
        cacheMap.clear();
    }

    public int size() {
        return this.cacheMap.size();
    }

    public void addKeyExtractorCommand(KeyExtractorCommand keyExtractorCommand) {
        Objects.requireNonNull(keyExtractorCommand, String.format(PARAMETER_CANNOT_BE_NULL, keyExtractorCommand));

        keyExtractorCommandMap.put(keyExtractorCommand.getClass(), keyExtractorCommand);
    }

    public void addCacheInvalidationCommand(InvalidationCommand invalidationCommand) {
        Objects.requireNonNull(invalidationCommand, String.format(PARAMETER_CANNOT_BE_NULL, invalidationCommand));

        invalidationCommandMap.put(invalidationCommand.getClass(), invalidationCommand);
    }

    public Map<Class<? extends KeyExtractorCommand>, KeyExtractorCommand> getKeyExtractorCommandMap() {
        return keyExtractorCommandMap;
    }

    public Map<Class<? extends InvalidationCommand>, InvalidationCommand> getInvalidationCommandMap() {
        return invalidationCommandMap;
    }

    private void initializeKeyExtractorCommandMap() {
        keyExtractorCommandMap = new ConcurrentHashMap<>();
        keyExtractorCommandMap.put(EntityKeyExtractorCommand.class, new EntityKeyExtractorCommand());
        keyExtractorCommandMap.put(QueryKeyExtractorCommand.class, new QueryKeyExtractorCommand());
    }

    private void initializeInvalidationCommandMap() {
        invalidationCommandMap = new ConcurrentHashMap<>();
        invalidationCommandMap.put(EntityKeyInvalidationCommand.class, new EntityKeyInvalidationCommand());
        invalidationCommandMap.put(QueryKeyInvalidationCommand.class, new QueryKeyInvalidationCommand());
    }

    private void autoClean() {
        if ((Float.valueOf(cacheMap.size()) / Float.valueOf(cacheSize)) * ONE_HUNDRED > MAX_CACHE_SIZE_PERCENTAGE) {
            if (Duration.between(lastClean, LocalDateTime.now()).toHours() <= TWO_HOURS) {
                var sb = new StringBuilder();
                sb.append("Biberante Cache is overloaded! ")
                  .append("Please check your settings and review if second level cache is enabled or what kind of ")
                  .append("requests can overload it. Also, check the cacheSize, you can extend this amount up to ")
                  .append(maxCacheSize);
                throw new CacheOverloadException(sb.toString());
            } else if (Duration.between(lastClean, LocalDateTime.now()).toDays() >= ONE_DAY) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> cacheMap.keySet().parallelStream()
                  .filter(key -> Duration.between(key.getUpdated(), LocalDateTime.now()).toDays() > ONE_DAY)
                  .forEach(cacheMap::remove));

                lastClean = LocalDateTime.now();
                executorService.shutdown();
            } else if (Duration.between(lastClean, LocalDateTime.now()).toDays() == ZERO_DAYS) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> cacheMap.keySet().parallelStream()
                  .filter(key -> Duration.between(key.getUpdated(), LocalDateTime.now()).toHours() > ONE_HOUR)
                  .forEach(cacheMap::remove));

                lastClean = LocalDateTime.now();
                executorService.shutdown();
            }
        }
    }

    private Optional<Key<?>> getKey(AbstractKeyParam<?> abstractKeyParam,
                                    Class<? extends KeyExtractorCommand> commandType) {
        return keyExtractorCommandMap.get(commandType).executeExtract(cacheMap, abstractKeyParam);
    }

}
