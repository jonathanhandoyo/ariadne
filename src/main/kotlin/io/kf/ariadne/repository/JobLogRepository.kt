package io.kf.ariadne.repository

import io.kf.ariadne.domain.JobLog
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface JobLogRepository: ReactiveMongoRepository<JobLog, String> {
    fun findAllByJobId(jobId: String): Flux<JobLog>
}