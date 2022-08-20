package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((auth) ->
                        auth.antMatchers("/usuario/logged", "/upload/foto")
                                .permitAll()
                                .antMatchers("/reembolso/update/{idReembolso}/usuario/{idUsuario}").hasAnyRole("ADMIN", "COLABORADOR")
                                .antMatchers("/reembolso/create").hasAnyRole("ADMIN", "COLABORADOR")
                                .antMatchers("/reembolso/logged/list/status").hasRole("COLABORADOR")
                                .antMatchers("/reembolso/list/status").hasAnyRole("ADMIN", "GESTOR", "FINANCEIRO")
                                .antMatchers("/reembolso/list/nome/status").hasAnyRole("ADMIN", "GESTOR", "FINANCEIRO")
                                .antMatchers("/reembolso/delete/{idReembolso}/usuario/{idUsuario}").hasAnyRole("ADMIN", "COLABORADOR")

                                .antMatchers("/gestor/aprovar/{idReembolso}").hasAnyRole("ADMIN", "GESTOR")

                                .antMatchers("/financeiro/pagar/{idReembolso}").hasAnyRole("ADMIN", "FINANCEIRO")

                                .antMatchers("/usuario/listar").hasRole("ADMIN")
                                .antMatchers("/usuario/listar/nome").hasRole("ADMIN")
                                .antMatchers("/usuario/delete/{idUsuario}").hasRole("ADMIN")

                                .antMatchers("/upload/anexo").hasAnyRole("ADMIN", "COLABORADOR")

                                .antMatchers("/admin/cadastro").hasRole("ADMIN")
                                .antMatchers("/admin/atribuir/role").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated());

        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/usuario/login",
                "/usuario/cadastro",
                "/");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}