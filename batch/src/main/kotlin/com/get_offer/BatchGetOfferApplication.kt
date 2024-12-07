package com.get_offer

import com.get_offer.batch.EndAuctionJobConfiguration
import com.get_offer.batch.StartAuctionJobConfiguration
import com.get_offer.common.EncryptProperties
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
@EnableConfigurationProperties(EncryptProperties::class)
class BatchGetOfferApplication(
    private val jobLauncher: JobLauncher,
    private val startAuctionJobConfiguration: StartAuctionJobConfiguration,
    private val endAuctionJobConfiguration: EndAuctionJobConfiguration
) {
    fun runJobs() {
        val logger = LoggerFactory.getLogger(BatchGetOfferApplication::class.java)
        try {
            jobLauncher.run(startAuctionJobConfiguration.startAuctionJob(), JobParameters())
            jobLauncher.run(endAuctionJobConfiguration.endAuctionJob(), JobParameters())
        } catch (e: Exception) {
            logger.error("Job execution failed: ${e.message}")
        }
    }
}

fun main(args: Array<String>) {
    val context = runApplication<BatchGetOfferApplication>(*args)

    // BatchApplication 클래스를 가져와서 runJobs 호출
    context.getBean(BatchGetOfferApplication::class.java).runJobs()

    // 실행 후 애플리케이션 종료
    context.close()
}
