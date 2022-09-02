package com.svydovets.bibirnate.cache.command.extractor;

import java.util.Map;
import java.util.Optional;

import com.svydovets.bibirnate.cache.key.Key;
import com.svydovets.bibirnate.cache.key.parameters.AbstractKeyParam;

/**
 * This interface is the start contract for '{@link Key} extractor' commands.
 */
public interface KeyExtractorCommand {

    /**
     * Extracts {@link Key} from the cacheMap {@link Map}. If {@link Key} is presented in the cacheMap then also make
     * update it (LRU) via {@link Key#update()}.
     *
     * @param cacheMap {@link Map} with cache
     * @param keyParam extension of the {@link AbstractKeyParam}
     * @return {@link Optional} of {@link Key} of {@link Optional#empty()}
     */
    Optional<Key<?>> executeExtract(Map<Key<?>, Object> cacheMap, AbstractKeyParam<?> keyParam);

}
