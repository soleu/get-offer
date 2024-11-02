package com.get_offer.login

import org.springframework.core.ResolvableType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate


@Controller
class LoginController(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val authorizedClientService: OAuth2AuthorizedClientService,
    private val loginService: LoginService,
) {

    @GetMapping("/oauth_login")
    fun getLoginPage(model: Model): String {
        val authorizationRequestBaseUri = "oauth2/authorization"
        val oauth2AuthenticationUrls: MutableMap<String, String> = HashMap()
        var clientRegistrations: Iterable<ClientRegistration>? = null
        val type = ResolvableType.forInstance(clientRegistrationRepository).`as`(Iterable::class.java)

        if (type != ResolvableType.NONE && ClientRegistration::class.java.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = clientRegistrationRepository as (Iterable<ClientRegistration>)
        }

        clientRegistrations?.forEach { registration ->
            oauth2AuthenticationUrls[registration.clientName] =
                "$authorizationRequestBaseUri/${registration.registrationId}"
        }

        model.addAttribute("urls", oauth2AuthenticationUrls)

        return "oauth_login"
    }

    @GetMapping("/loginSuccess")
    fun loadUser(model: Model, authentication: OAuth2AuthenticationToken): String {
        val client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
            authentication.authorizedClientRegistrationId, authentication.name
        )

        val userInfoEndpointUri = client.clientRegistration.providerDetails.userInfoEndpoint.uri

        if (!userInfoEndpointUri.isNullOrEmpty()) {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders().apply {
                add(HttpHeaders.AUTHORIZATION, "Bearer ${client.accessToken.tokenValue}")
            }

            val entity = HttpEntity("", headers)
            val response = restTemplate.exchange(
                userInfoEndpointUri, HttpMethod.GET, entity, GoogleUser::class.java
            )

            val authMember = response.body ?: throw IllegalStateException()
            val token = loginService.sign(authMember)

            model.addAttribute("name", response.body?.name)
            model.addAttribute("Token", token)
        }
        return "loginSuccess"
    }
}