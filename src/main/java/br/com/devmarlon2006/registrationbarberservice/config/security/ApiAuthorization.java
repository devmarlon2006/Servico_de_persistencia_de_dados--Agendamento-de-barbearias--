package br.com.devmarlon2006.registrationbarberservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApiAuthorization {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf( AbstractHttpConfigurer::disable );
        security.httpBasic( Customizer.withDefaults() );
        security.authorizeHttpRequests( authorize -> authorize.anyRequest().authenticated());

        return security.build();
    }

}
