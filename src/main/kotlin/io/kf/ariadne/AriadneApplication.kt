package io.kf.ariadne

import io.kf.ariadne.service.JobService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class AriadneApplication(val jobService: JobService) {
    val logger: Logger = LoggerFactory.getLogger(AriadneApplication::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady(event: ApplicationReadyEvent) {
        logger.info(">> application >> ready")
        this.jobService.initialize()
    }

}

fun main(args: Array<String>) {
    runApplication<AriadneApplication>(*args)
}
