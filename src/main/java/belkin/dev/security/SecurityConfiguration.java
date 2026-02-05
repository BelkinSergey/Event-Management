package belkin.dev.security;


import belkin.dev.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST, "/locations")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/locations/**")
                                .hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/locations")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/locations")
                                .hasAnyAuthority("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/users/{id}")
                                .hasAnyAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth")
                                .permitAll()

                                .requestMatchers(HttpMethod.POST, "/events")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/events/{id}")
                                .hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/events/{id}")
                                .hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/events/{id}")
                                .hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/events/search")
                                .hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/events/my")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/{id}")
                                .hasAnyAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/events/registrations/my")
                                .hasAnyAuthority("USER")


                                .requestMatchers(HttpMethod.POST, "/events/registrations/{id}")
                                .hasAnyAuthority("USER")

                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )

                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
