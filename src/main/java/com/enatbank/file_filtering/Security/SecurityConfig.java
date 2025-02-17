//package com.enatbank.file_filtering.Security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.web.SecurityFilterChain;
//@Configuration
//public class SecurityConfig {
////
////        @Bean
////        public SecurityFilterChain securityFilterChain(HttpSecurity http,
////                                                       ClientRegistrationRepository clientRegistrationRepository) throws Exception {
////            http
////                    .authorizeHttpRequests(auth -> auth
////                            .requestMatchers("/admin").hasRole("ADMIN")
////                            .requestMatchers("/user").hasRole("USER")
////                            .anyRequest().authenticated()
////                    )
////                    .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/home", true))
////                    .logout(logout -> logout
////                            .logoutSuccessHandler(
////                                    new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository)
////                            )
////                    );
////
////            return http.build();
////        }
//
//
//
//}
