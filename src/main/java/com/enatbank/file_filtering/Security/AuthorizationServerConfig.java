//package com.enatbank.file_filtering.Security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.time.Duration;
//import java.util.UUID;
//
//import static org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration.applyDefaultSecurity;
//
//@Configuration
//@EnableWebSecurity
//public class AuthorizationServerConfig {
//    @Bean
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        return http.formLogin().and().build();
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService() {
////        UserDetails user = User.withDefaultPasswordEncoder()
////                .username("testuser")
////                .password("password")
////                .roles("USER")
////                .build();
////        return new InMemoryUserDetailsManager(user);
////    }
////
////    @Bean
////    public AuthorizationServerSettings authorizationServerSettings() {
////        return AuthorizationServerSettings.builder()
////                .issuer("http://localhost:9000") // Authorization Server URL
////                .build();
////    }
////
////    @Bean
////    public RegisteredClientRepository registeredClientRepository() {
////        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
////                .clientId("client-id")
////                .clientSecret("{noop}client-secret") // {noop} means no encoding
////                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
////                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
////                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
////                .redirectUri("http://localhost:8081/login/oauth2/code/client-id") // OAuth2 Client
////                .scope(OidcScopes.OPENID)
////                .scope("read")
////                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
////                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(1)).build())
////                .build();
////
////        return new InMemoryRegisteredClientRepository(registeredClient);
////    }
//
//}
