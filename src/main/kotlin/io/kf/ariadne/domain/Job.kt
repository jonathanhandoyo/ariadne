package io.kf.ariadne.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.http.HttpMethod

@Document(collection = "job")
data class Job(@Id val id: String? = null,
               val code: String,
               val description: String,
               val type: Type,
               val status: Status = Status.STOPPED,
               val detail: Detail) {

    enum class Status {
        READY,
        STARTED,
        SUSPENDED,
        STOPPED
    }

    enum class Type {
        FIXED_RATE,
        UNKNOWN
    }

    data class Detail(val method: HttpMethod,
                      val uri: String,
                      val period: Long)
}