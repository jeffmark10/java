package com.suaempresa.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt, o padrão moderno (substituto do generate_password_hash)
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a estas rotas e recursos estáticos
                .requestMatchers("/", "/inicio", "/login", "/cadastro", "/css/**", "/js/**", "/images/**", "/contato", "/sobre").permitAll()
                // Protege as rotas de admin, exigindo o papel 'ADMIN'
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Qualquer outra requisição exige que o usuário esteja autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // Usa a página de login customizada
                .loginPage("/login")
                .loginProcessingUrl("/perform_login") // URL que o formulário de login deve enviar
                .defaultSuccessUrl("/inicio", true) // Redireciona para o início após o login
                .failureUrl("/login?error=true") // Redireciona em caso de falha
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/inicio?logout=true") // Redireciona após o logout
                .permitAll()
            );

        return http.build();
    }
}