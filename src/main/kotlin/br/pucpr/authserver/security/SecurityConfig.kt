package br.pucpr.authserver.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

// configuracao da cadeia de seguraca

@Configuration
@EnableMethodSecurity
@SecurityScheme(
    name = "jwt-auth", // pode ser utilizado qualquer nome
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
)
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter
) {
    @Bean
    fun filterChain(security: HttpSecurity): SecurityFilterChain =
        security
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors(Customizer.withDefaults())
            .csrf { it.disable() } // desabilita validacao com navegador
            .headers { it.frameOptions { fo -> fo.disable() } } // desabilita protecao de frame options
            .authorizeHttpRequests { requests ->
                // configuracao permissao de metodos
                requests
                    .requestMatchers(HttpMethod.GET).permitAll()
                    .requestMatchers(HttpMethod.POST, "/users").permitAll()
                    .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/phone-auth").permitAll()
                    .requestMatchers(HttpMethod.POST, "/phone-auth/confirm").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
            .build()

    @Bean
    fun corsFilter() = CorsConfiguration().apply {
        addAllowedHeader("*")
        addAllowedMethod("*")
        addAllowedOrigin("*")
    }.let {
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", it)
        }
    }.let {
        CorsFilter(it)
    }

}