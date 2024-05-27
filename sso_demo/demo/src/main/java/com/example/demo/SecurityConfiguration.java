package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Order(2)
public class SecurityConfiguration {

    private static final String METADATA_URL = "https://login.microsoftonline.com/d211aab8-d4bc-4e58-b090-fb720f0a0965/federationmetadata/2007-06/federationmetadata.xml?appid=a0b0dae1-410a-422a-8b31-c6eb228e2896";

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
        return new InMemoryRelyingPartyRegistrationRepository(
                RelyingPartyRegistrations
                        .fromMetadataLocation(METADATA_URL)
                        .registrationId("home")
                        .entityId("api://a0b0dae1-410a-422a-8b31-c6eb228e2896")
                        .assertionConsumerServiceLocation("http://localhost:8080/login/saml2/sso/home")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                )
                .saml2Login(saml2 -> saml2
                        .loginProcessingUrl("/login/saml2/sso/home") // Ensure this URL matches the login processing URL
                );

        return http.build();
    }
}
