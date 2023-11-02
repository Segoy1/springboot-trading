package de.segoy.springboottradingweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static de.segoy.springboottradingweb.security.SecurityRoles.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector).servletPath("/");
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers(mvc.pattern("/"),mvc.pattern("/home")).permitAll()
                .requestMatchers(mvc.pattern("/employees")).hasRole(EMPLOYEES_PAG_VIEW)
                .requestMatchers(mvc.pattern("/departments")).hasRole(DEPARTMENTS_PAG_VIEW)
                .requestMatchers(mvc.pattern("/customers")).hasRole(CUSTOMERS_PAG_VIEW)
                .anyRequest().authenticated())
                .formLogin(form  -> form
                        .loginPage("/login")
                        .failureUrl("/login-error")
                        .permitAll())
                .logout(form -> form
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());

        return http.build();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("john")
                .password(encoder.encode("john"))
                .roles(SUPER_ADMIN)
                .and()
                .withUser("emma")
                .password(encoder.encode("emma"))
                .roles(EMPLOYEES_ADMIN)
                .and()
                .withUser("william")
                .password(encoder.encode("william"))
                .roles(DEPARTMENTS_PAG_VIEW, DEPARTMENTS_READ, DEPARTMENTS_CREATE)
                .and()
                .withUser("lucas")
                .password(encoder.encode("lucas"))
                .roles(CUSTOMERS_PAG_VIEW, CUSTOMERS_READ)
                .and()
                .withUser("tom")
                .password(encoder.encode("tom"))
                .roles();
    }
}
