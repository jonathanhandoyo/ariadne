package io.kf.ariadne.repository

import io.kf.ariadne.domain.Job
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface JobRepository: ReactiveMongoRepository<Job, String> {
    fun findAllByStatus(status: Job.Status): Flux<Job>
}