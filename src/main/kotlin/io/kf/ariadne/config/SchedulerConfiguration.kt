@file:Suppress("UsePropertyAccessSyntax")

package io.kf.ariadne.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@Configuration
class SchedulerConfiguration {

    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler()
                .apply { setThreadNamePrefix("ariadne-t-") }
                .apply { setPoolSize(99) }
                .also { it.initialize() }
    }
}