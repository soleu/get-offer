package com.get_offer.batch

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BatchScheduler(
    private val startAuctionJobConfiguration: StartAuctionJobConfiguration,
    private val endAuctionJobConfiguration: EndAuctionJobConfiguration,
) {
    @Autowired
    private lateinit var jobLauncher: JobLauncher
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 3) // 3시간 주기
    fun run() {
        try {
            jobLauncher.run(startAuctionJobConfiguration.startAuctionJob(), JobParameters())
            jobLauncher.run(endAuctionJobConfiguration.endAuctionJob(), JobParameters())
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }
}