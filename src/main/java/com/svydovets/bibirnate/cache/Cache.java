package com.svydovets.bibirnate.cache;


import static com.svydovets.bibirnate.cache.constant.CacheConstant.PARAMETER_CANNOT_BE_NULL;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import com.svydovets.bibirnate.cache.command.extractor.KeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.EntityKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.extractor.impl.QueryKeyExtractorCommand;
import com.svydovets.bibirnate.cache.command.invalidation.InvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.EntityKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.command.invalidation.impl.QueryKeyInvalidationCommand;
import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;
import com.svydovets.bibirnate.exceptions.CacheOverloadException;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is LRU cache realization.
 * LRU builds on the {@link Key#getUpdated()} field. Each time when we extract a {@link Key} inside the
 * {@link KeyExtractorCommand} realisation we do {@link Key#update()}.
 * For controlling cache state we have a mechanism that makes autoClean in the
 * {@link Cache#put(AbstractKeyParam, Object)}.
 * Cache is constructed on the {@link ConcurrentHashMap}. This map consists from the {@link Key} (that is the custom
 * class) and {@link Object} as value (we are able to put an entity, collection or other types of objects).
 * For extraction from the cache is the method {@link Cache#get(AbstractKeyParam, Class)} where the second parameter is
 * the realization of the {@link KeyExtractorCommand}. This way was chosen for the case if we provide a new extension
 * of the {@link AbstractKeyParam}, then we can provide a new command for extraction and add it into cache via
 * {@link Cache#addKeyExtractorCommand(KeyExtractorCommand)}.
 * For invalidation the same story as with extraction. It also works via command. But here the logic a bit more
 * complicated because cache is able to invalidate related caches by {@link AbstractKeyParam#getEntityType()}.
 */
@Slf4j
public class Cache {

    private static final int DEFAULT_CACHE_SIZE = 10_000;
    private static final int MIN_CACHE_SIZE = 20;
    private static final int DEFAULT_MAX_CACHE_SIZE = 200_000;
    private static final int ZERO_DAYS = 0;
    private static final int ONE_DAY = 1;
    private static final int ONE_HOUR = 1;
    private static final int TWO_HOURS = 2;
    private static final int SIXTY_SECONDS = 60;
    private static final int ONE_HUNDRED = 100;
    private static final int MAX_CACHE_SIZE_PERCENTAGE = 85;
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
        log.trace("Creation new Cache with cacheSize [{}]", cacheSize);
        if ((maxCacheSize < MIN_CACHE_SIZE * 2) || cacheSize < MIN_CACHE_SIZE || cacheSize > maxCacheSize) {
            log.error("Provided cacheSize [{}] has inappropriate state", cacheSize);
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
        log.trace("Creation new Cache is finished");
    }

    /**
     * Cannot apply null parameter as <b>keyParam</b>.
     * Put into cache a value by {@link Key}. The {@link Key} will be generated inside from the passed <b>keyParam</b>.
     * Also, inside included a mechanism that will make auto-clean for cache. Auto-cleaning will be started in case
     * if the cache map loaded more than on the 85% of size. Auto-clean will firstly remove caches that loaded one
     * day ago and more. In case if auto-clean was performed today but more than 2 hours from last clean, then it will
     * invalidate all caches that were written more than one hour ago. In case if from last clean and new clean duration
     * less than two hours - will be thrown the {@link CacheOverloadException}. Cache auto-clean starts in a new thread
     * (works on the background).
     *
     * @param keyParam instance of the {@link AbstractKeyParam}
     * @param value    value that should be cached
     */
    public void put(AbstractKeyParam<?> keyParam, Object value) {
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));

        cacheMap.put(new Key<>(keyParam), value);

        autoClean();
    }

    /**
     * Extracts cached value by the {@link Key}. The {@link Key} extracts from the cacheMap <b>keyParam</b> and
     * <b>commandType</b> for extraction. Then, when the {@link Key} is found we extract from the cacheMap by this
     * key a value.
     * All passed parameters should be not null.
     *
     * @param keyParam           extension of the {@link AbstractKeyParam}
     * @param extractCommandType {@link Class} of the implementation of the {@link KeyExtractorCommand}
     * @return {@link Optional#of(Object)} cached value or {@link Optional#empty()}
     */
    public Optional<Object> get(AbstractKeyParam<?> keyParam, Class<? extends KeyExtractorCommand> extractCommandType) {
        Objects.requireNonNull(keyParam, String.format(PARAMETER_CANNOT_BE_NULL, keyParam));
        Objects.requireNonNull(extractCommandType, String.format(PARAMETER_CANNOT_BE_NULL, extractCommandType));

        Optional<Key<?>> key = getKey(keyParam, extractCommandType);

        return key.map(cacheMap::get);
    }

    /**
     * All passed parameters should be not null.
     * This method invalidate cache. Firstly it extracts the a {@link Key} by its {@link AbstractKeyParam} via
     * implementation of the {@link KeyExtractorCommand}. Then it's invalidating cache by this key and all related
     * caches. For defining the related caches the method uses {@link AbstractKeyParam#getEntityType()} field.
     *
     * @param keyParam                extension of the {@link AbstractKeyParam}
     * @param extractCommandType      {@link Class} of the implementation of the {@link KeyExtractorCommand}
     * @param invalidationCommandType {@link Class} of the implementation of the {@link InvalidationCommand}
     */
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

    /**
     * Provides actual size of the cacheMap.
     *
     * @return size of cache
     */
    public int size() {
        return this.cacheMap.size();
    }

    /**
     * Add to the <b>keyExtractorCommandMap</b> the new realization of the {@link KeyExtractorCommand}.
     *
     * @param keyExtractorCommand instance of the {@link KeyExtractorCommand}
     */
    public void addKeyExtractorCommand(KeyExtractorCommand keyExtractorCommand) {
        Objects.requireNonNull(keyExtractorCommand, String.format(PARAMETER_CANNOT_BE_NULL, keyExtractorCommand));

        keyExtractorCommandMap.put(keyExtractorCommand.getClass(), keyExtractorCommand);
    }

    /**
     * Add to the <b>invalidationCommand</b> the new realization of the {@link InvalidationCommand}.
     *
     * @param invalidationCommand instance of the {@link InvalidationCommand}
     */
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

    public Set<Key<?>> getKeys() {
        return cacheMap.keySet();
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
            if (Duration.between(lastClean, LocalDateTime.now()).toSeconds() <= SIXTY_SECONDS) {
                if (Duration.between(lastClean, LocalDateTime.now()).toHours() <= TWO_HOURS) {
                    var sb = new StringBuilder();
                    sb.append("Biberante Cache is overloaded! ")
                      .append("Please check your settings and review if second level cache is enabled or what kind of ")
                      .append("requests can overload it. Also, check the cacheSize, you can extend this amount up to ")
                      .append(maxCacheSize);
                    throw new CacheOverloadException(sb.toString());
                } else if (Duration.between(lastClean, LocalDateTime.now()).toDays() >= ONE_DAY) {
                    clean(key -> Duration.between(key.getUpdated(), LocalDateTime.now()).toDays() > ONE_DAY);
                } else if (Duration.between(lastClean, LocalDateTime.now()).toDays() == ZERO_DAYS) {
                    clean(key -> Duration.between(key.getUpdated(), LocalDateTime.now()).toHours() > ONE_HOUR);
                }
            }
        }
    }

    private void clean(Predicate<? super Key<?>> predicate) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> cacheMap.keySet().parallelStream()
          .filter(predicate)
          .forEach(cacheMap::remove));

        lastClean = LocalDateTime.now();
        executorService.shutdown();
    }

    private Optional<Key<?>> getKey(AbstractKeyParam<?> abstractKeyParam,
                                    Class<? extends KeyExtractorCommand> commandType) {
        return keyExtractorCommandMap.get(commandType).executeExtract(cacheMap, abstractKeyParam);
    }

}
