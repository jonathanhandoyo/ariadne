package io.kf.ariadne.service

import io.kf.ariadne.domain.Job
import io.kf.ariadne.domain.JobLog
import io.kf.ariadne.repository.JobLogRepository
import io.kf.ariadne.repository.JobRepository
import io.netty.channel.ChannelOption
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Service
class JobService(val taskScheduler: ThreadPoolTaskScheduler,
                 val jobRepository: JobRepository,
                 val jobLogRepository: JobLogRepository) {

    val logger: Logger = LoggerFactory.getLogger(JobService::class.java)
    val connector: ClientHttpConnector = ReactorClientHttpConnector { it.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) }
    val builder: WebClient.Builder = WebClient.builder()

    fun initialize() {
        this.restartJobs()
        this.taskScheduler.scheduleAtFixedRate({ this.rescanJobs() }, 20_000)
    }

    fun startJob(job: Job) {
        logger.info(">> job >> $job")
        this.jobLogRepository.insert(JobLog(jobId = job.id!!, startTime = LocalDateTime.now(), status = JobLog.Status.STARTED))
                .flatMap { log ->
                    this.builder
                            .clientConnector(this.connector)
                            .baseUrl(job.detail.uri).build()
                            .method(job.detail.method).uri { builder -> builder.queryParam("id", log.id!!).build() }
                            .exchange()
                }
                .subscribe()
    }

    fun restartJobs() {
        this.jobRepository.findAllByStatus(Job.Status.STARTED)
                .doOnNext { it ->
                    logger.info(">> job >> ${it.code} << restarted")
                    this.taskScheduler.scheduleAtFixedRate({ this.startJob(it) }, it.detail.period)
                }
                .subscribe()
    }

    fun rescanJobs() {
        this.jobRepository.findAllByStatus(Job.Status.READY)
                .doOnNext { it ->
                    logger.info(">> job >> ${it.code} << picked up")
                    this.taskScheduler.scheduleAtFixedRate({ this.startJob(it) }, it.detail.period)
                    this.jobRepository.save(it.copy(status = Job.Status.STARTED)).subscribe()
                }
                .subscribe()
    }
}