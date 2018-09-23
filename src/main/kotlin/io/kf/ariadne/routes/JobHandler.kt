package io.kf.ariadne.routes

import io.kf.ariadne.domain.JobLog
import io.kf.ariadne.repository.JobLogRepository
import io.kf.ariadne.repository.JobRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component
class JobHandler(val jobRepository: JobRepository,
                 val jobLogRepository: JobLogRepository) {

    fun findAllJobs(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.jobRepository.findAll())
    }

    fun findJobById(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.jobRepository.findById(request.pathVariable("jobId")))
    }

    fun createJob(request: ServerRequest): Mono<ServerResponse> = ServerResponse.ok().build() // TODO
    fun updateJob(request: ServerRequest): Mono<ServerResponse> = ServerResponse.ok().build() // TODO

    fun findAllJobLogsByJobId(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.jobLogRepository.findAllByJobId(request.pathVariable("jobId")))
    }

    fun findJobLogById(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.jobLogRepository.findById(request.pathVariable("logId")))
    }

    fun updateJobLog(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .build(this.jobLogRepository
                        .findById(request.pathVariable("logId"))
                        .flatMap { log ->
                            this.jobLogRepository.save(log.copy(
                                    endTime = LocalDateTime.now(),
                                    status = JobLog.Status.valueOf(request.pathVariable("status"))
                            ))
                        }
                        .then()
                )
    }
}