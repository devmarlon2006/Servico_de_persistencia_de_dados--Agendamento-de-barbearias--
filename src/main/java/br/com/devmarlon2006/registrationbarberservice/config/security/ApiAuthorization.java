package br.com.devmarlon2006.registrationbarberservice.config.security;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.config.security.customsuserdetails.CustomBarberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApiAuthorization {

    @Value( "${endpoints.persistence}" + "/**" )
    String persistencePath;

    @Value( "${endpoints.persistence}" + "${api.entity's.barberShop}" )
    String BarberShopPersistencePath;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        System.out.println("Security Filter Chain");

        security.csrf( AbstractHttpConfigurer::disable );

        security.httpBasic( Customizer.withDefaults() );

        security.authorizeHttpRequests( authorize -> authorize
                .requestMatchers( HttpMethod.POST , BarberShopPersistencePath ).hasRole( "BARBEIRO" )
                .requestMatchers( HttpMethod.POST , persistencePath ).permitAll()
            .anyRequest().authenticated()
        );

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Mais seguro que o -> MD5
    }

    @Bean
    public UserDetailsService userDetailsService (BarberRepository barberRepositoryManagerService) {
        return new CustomBarberDetailsService( barberRepositoryManagerService );
    }

    @Bean
    public CommandLineRunner testPasswordMatcher(PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. A senha em texto puro que você está digitando no Postman
            String rawPassword = "senha_em_texto_puro";

            // 2. O HASH exato que está no seu banco de dados (copie e cole)
            String encodedPasswordFromDB = "$2a$10$mcwNUIeTMdlj2z9S9gg1HOnrsoh8satdZPKOVZk2DNc56VAGATR6i";

            boolean matches = passwordEncoder.matches(rawPassword, encodedPasswordFromDB);

            System.out.println("TESTE CRÍTICO DE SENHA: O HASH CORRESPONDE? " + matches);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        // Opcional, mas útil: Forçar a verificação de existência do usuário antes de checar a senha
        provider.setHideUserNotFoundExceptions(false);

        return provider;
    }

}
