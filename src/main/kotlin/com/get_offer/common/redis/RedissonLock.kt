package com.get_offer.common.redis

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RedissonLock(
    val value: String, // Lock 이름
    val waitTime: Long = 5000L, // Lock 획득을 시도하는 최대 시간(ms)
    val leaseTime: Long = 2000L // 락을 획득한 후 점유하는 최대 시간(ms)
)