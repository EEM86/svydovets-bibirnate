package com.svydovets.bibirnate.cache.constant;

/**
 * Provides constant values for cache.
 */
public final class CacheConstant {

    private CacheConstant() {
    }

    public static final String PARAMETER_CANNOT_BE_NULL = "Parameter [%s] cannot be null!";
    public static final String CHECK_PASSED_PARAMETERS_ON_NULL_START_MESSAGE =
      "checkPassedParametersOnNull, start validation parameters on null...";
    public static final String CHECK_PASSED_PARAMETERS_ON_NULL_FINISH_MESSAGE =
      "checkPassedParametersOnNull, validation parameters on null is finished. All passed parameters are not null.";
    public static final String GENERATE_KEY_PARAM_VALIDATION_START_MESSAGE =
      "generateKeyParam, start validation parameters...";
    public static final String GENERATE_KEY_PARAM_VALIDATION_FINISH_MESSAGE =
      "generateKeyParam, validation parameters is passed. Start creation KeyParam...";
    public static final String GENERATE_KEY_PARAM_FINISH_MESSAGE =
      "generateKeyParam, new [{}] is successfully created.";
    public static final String INVALIDATE_FIRST_CACHE_START_MESSAGE =
      "invalidate, start invalidation for the first level cache...";
    public static final String INVALIDATE_FIRST_CACHE_FINISH_MESSAGE =
      "invalidate, invalidation for the first level cache is successfully finished.";
    public static final String INVALIDATE_SECOND_CACHE_START_MESSAGE =
      "invalidate, start invalidation for the second level cache...";
    public static final String INVALIDATE_SECOND_CACHE_FINISH_MESSAGE =
      "invalidate, invalidation for the second level cache is successfully finished.";

}
