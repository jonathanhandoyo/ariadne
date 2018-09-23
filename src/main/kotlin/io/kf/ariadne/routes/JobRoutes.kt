package io.kf.ariadne.routes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class JobRoutes(val jobHandler: JobHandler) {

    @Bean
    fun router() = router {
        ("/jobs").nest {
            GET("/", jobHandler::findAllJobs)
            POST("/", jobHandler::createJob)
            PUT("/", jobHandler::updateJob)
            ("/{jobId}").nest {
                GET("/", jobHandler::findJobById)
                ("/logs").nest {
                    GET("/", jobHandler::findAllJobLogsByJobId)
                    ("/{logId}").nest {
                        GET("/", jobHandler::findJobLogById)
                        GET("/status/{status}", jobHandler::updateJobLog)
                    }
                }
            }
        }
    }
}