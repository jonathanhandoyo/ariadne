package io.kf.ariadne.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "job-log")
data class JobLog(@Id val id: String? = null,
                  val jobId: String,
                  val status: Status,
                  val startTime: LocalDateTime,
                  val endTime: LocalDateTime? = null) {
    enum class Status {
        STARTED,
        COMPLETE,
        FAILED,
        TERMINATED
    }
}