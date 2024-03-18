package com.alexmacro.bookinfo.configs;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.key}")
    private String jwtKey;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth").hasRole("USER")
                        .anyRequest().hasAuthority("SCOPE_READ")
                )
                .sessionManagement(session->session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
                .oauth2ResourceServer(resourceServer-> resourceServer.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    //User only for demostration, do NOT expose sensitive information like passwords
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withUsername("user4")
                .password("{noop}pass4#")
                .authorities("READ", "ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        byte[] bytes = jwtKey.getBytes();
        SecretKeySpec originalKey= new SecretKeySpec(bytes,0, bytes.length,"RSA");
        return NimbusJwtDecoder.withSecretKey(originalKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

}