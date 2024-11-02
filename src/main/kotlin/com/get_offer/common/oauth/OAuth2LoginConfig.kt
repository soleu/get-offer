package com.get_offer.common.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.web.SecurityFilterChain


@Configuration
class OAuth2LoginConfig(
    @Value("\${spring.security.oauth2.client.registration.google-login.client-id}")
    private val clientId: String,
    @Value("\${spring.security.oauth2.client.registration.google-login.client-secret}")
    private val clientSecret: String,
) {
    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        return InMemoryClientRegistrationRepository(googleClientRegistration())
    }

    private fun googleClientRegistration(): ClientRegistration {
        val baseUrl = "http://localhost:8080"
        val registrationId = "google"
        return ClientRegistration.withRegistrationId("google").clientId(clientId).clientSecret(clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("${baseUrl}/login/oauth2/code/${registrationId}")
            .scope("openid", "profile", "email", "address", "phone")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://www.googleapis.com/oauth2/v4/token")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo").userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs").clientName("Google").build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorizeHttpRequests ->
            authorizeHttpRequests.requestMatchers("/oauth_login", "/loginSuccess").permitAll()
                .anyRequest()
                .authenticated()
        }.oauth2Login {
            it.loginPage("/oauth_login")
            it.defaultSuccessUrl("/loginSuccess", true)
        }
        return http.build()
    }

    @Bean
    fun accessTokenResponseClient(): OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
        return DefaultAuthorizationCodeTokenResponseClient()
    }
}