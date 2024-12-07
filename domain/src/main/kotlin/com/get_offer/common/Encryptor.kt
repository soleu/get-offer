package com.get_offer.common

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class Encryptor(val encryptProperties: EncryptProperties) {

    @Bean("jasyptStringEncryptor")
    fun jasyptEncryptor(): PooledPBEStringEncryptor {
        val pooledPBEStringEncryptor = PooledPBEStringEncryptor()
        return pooledPBEStringEncryptor.apply {
            setAlgorithm(encryptProperties.algorithm)
            setPoolSize(encryptProperties.poolSize)
            setStringOutputType(encryptProperties.stringOutputType)
            setKeyObtentionIterations(encryptProperties.keyObtentionIterations)
            setPassword(encryptProperties.password)
        }
    }

}