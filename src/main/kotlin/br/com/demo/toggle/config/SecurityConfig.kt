package br.com.demo.toggle.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable().authorizeRequests {
            it.antMatchers("/**/togglz-console/**").hasAuthority("ADMIN").and()
                    .formLogin()
                    .successForwardUrl("/togglz-console")
                    .and()
                    .logout()
                    .deleteCookies("JSESSIONID")
            it.antMatchers("/**/toggle/").hasAuthority("USER").and().httpBasic()
            it.antMatchers("/**/toggle/**").permitAll()
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val admin = User.builder()
                .username("admin")
                .password("{bcrypt}\$2a\$10\$4HcjbGxDlhFOVVDi/yR/Vehj6MNJrWSbncIz6p6jbJvcbOCe3Jf/y")
                .authorities("ADMIN").build()
        val user = User.builder()
                .username("user")
                .password("{bcrypt}\$2a\$10\$4HcjbGxDlhFOVVDi/yR/Vehj6MNJrWSbncIz6p6jbJvcbOCe3Jf/y")
                .authorities("USER").build()
        return InMemoryUserDetailsManager(admin, user)
    }
}