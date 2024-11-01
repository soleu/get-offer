package com.get_offer.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/batch")
class BatchController(
    private val jobLauncher: JobLauncher,
    private val startAuctionJob: Job,
) {
    @PostMapping("/start")
    fun startBatch(): String {
        val jobParameters = JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(startAuctionJob, jobParameters)
        return "Start Auction Batch Job started"
    }
}