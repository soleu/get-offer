package com.get_offer.common.redis

import com.get_offer.common.logger
import java.util.concurrent.TimeUnit
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(1) // 락을 트랙잭션보다 먼저 실행
class RedissonLockAspect(
    private val redissonClient: RedissonClient
) {
    private val log = logger()

    @Around("@annotation(com.get_offer.common.redis.RedissonLock))")
    fun redissonLock(joinPoint: ProceedingJoinPoint) {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method = signature.method
        val annotation = method.getAnnotation(RedissonLock::class.java)
        val lockKey = method.name + CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            annotation.value
        )
        val lock: RLock = redissonClient.getLock(lockKey)

        try {
            val lockable = lock.tryLock(annotation.waitTime, annotation.leaseTime, TimeUnit.MILLISECONDS)
            if (!lockable) {
                log.info("Lock 획득 실패={}", lockKey)
                return
            }
            joinPoint.proceed()
        } catch (e: InterruptedException) {
            throw e
        } finally {
            lock.unlock()
        }
    }
}
