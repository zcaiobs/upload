package br.com.demo.toggle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.togglz.core.user.UserProvider
import org.togglz.servlet.user.ServletUserProvider

@Configuration
class ToggleConfig {

    @Bean
    fun userProvider(): UserProvider {
        return ServletUserProvider("ADMIN")
    }
}