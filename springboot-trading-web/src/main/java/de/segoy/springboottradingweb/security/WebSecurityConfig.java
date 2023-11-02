package de.segoy.springboottradingweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.sql.DataSource;

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

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails user = User.builder()
                .username("john")
                .password(encoder.encode("john"))
                .roles(SUPER_ADMIN)
                .build();
        UserDetails user2 = User.builder()
                .username("emma")
                .password(encoder.encode("emma"))
                .roles(EMPLOYEES_ADMIN)
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(user2);
        return users;
    }
}
