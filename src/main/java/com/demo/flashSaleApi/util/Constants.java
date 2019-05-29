package com.demo.flashSaleApi.util;

public final class Constants {

	public static final String LOCK_PREFIX = "lock";
    public static final Long LOCK_ACQUIRE_TIMEOUT = 10L;
    public static final Integer LOCK_ACQUIRE_LOOP_SLEEP = 1;
    public static final Integer LOCK_RELEASE_LOOP_SLEEP = 100;
    public static final Integer PURCHASE_CACHE_CYCLE_SLEEP = 1;

    public static final String FLASHSALE_CACHE_PREFIX = "flashsale";
    public static final String PRODUCT_CACHE_PREFIX = "item_count";
    public static final String BUYER_CACHE_PREFIX = "buyer_status";

    public static final String ELIGIBILITY_LOCKNAME = "eligibility";
    public static final Long ELIGIBILITY_LOCK_TIMEOUT = 10L;
    public static final String BUY_LOCKNAME = "buy";
    public static final Long BUY_LOCK_TIMEOUT = 10L;

    // In seconds
    public static final Integer FLASHSALE_DURATION = 5 * 60;
    public static final Integer BUYER_ELIGIBILITY_QUERY_TIMEOUT = 10;
    public static final Integer BUY_TIMEOUT = 60;
    public static final Integer DEFAULT_CACHE_TIMEOUT_IN_SECONDS = 5 * 60;
}
