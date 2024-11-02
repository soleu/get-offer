package com.get_offer.user

import com.get_offer.user.controller.TokenRequest
import org.springframework.core.ResolvableType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate


@Controller
class LoginController(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val accessTokenResponseClient: OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>,
    private val authorizedClientService: OAuth2AuthorizedClientService,
) {

    @GetMapping("/oauth_login")
    fun getLoginPage(model: Model): String {
        val authorizationRequestBaseUri = "oauth2/authorization"
        val oauth2AuthenticationUrls: MutableMap<String, String> = HashMap()
        var clientRegistrations: Iterable<ClientRegistration>? = null
        val type = ResolvableType.forInstance(clientRegistrationRepository)
            .`as`(Iterable::class.java)

        if (type != ResolvableType.NONE &&
            ClientRegistration::class.java.isAssignableFrom(type.resolveGenerics()[0])
        ) {
            clientRegistrations = clientRegistrationRepository as Iterable<ClientRegistration>
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

        val userInfoEndpointUri = client.clientRegistration
            .providerDetails.userInfoEndpoint.uri

        if (!userInfoEndpointUri.isNullOrEmpty()) {
            val restTemplate = RestTemplate()
            val headers = HttpHeaders().apply {
                add(HttpHeaders.AUTHORIZATION, "Bearer ${client.accessToken.tokenValue}")
            }
            println(client.accessToken.tokenValue) // 후에 수정 필요
            val entity = HttpEntity("", headers)
            val response = restTemplate.exchange(
                userInfoEndpointUri,
                HttpMethod.GET,
                entity,
                Map::class.java
            )
            val userAttributes = response.body ?: emptyMap<String, Any>()
            model.addAttribute("name", userAttributes["name"])
        }
        return "loginSuccess"
    }

    @PostMapping("/token")
    fun getAccessToken(@RequestBody req: TokenRequest): String? {
        val clientRegistration = clientRegistrationRepository.findByRegistrationId("google")

        val authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(clientRegistration.clientId)
            .redirectUri(req.redirectUri)
            .authorizationUri(clientRegistration.providerDetails.authorizationUri)
            .build()

        val authorizationResponse = OAuth2AuthorizationResponse.success(req.authorizationCode)
            .redirectUri(req.redirectUri)
            .build()

        val authorizationExchange = OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse)

        // Access Token 발급 요청
        val tokenRequest = OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange)
        val tokenResponse = accessTokenResponseClient.getTokenResponse(tokenRequest)

        // Access Token 반환
        return tokenResponse.accessToken.tokenValue
    }
}