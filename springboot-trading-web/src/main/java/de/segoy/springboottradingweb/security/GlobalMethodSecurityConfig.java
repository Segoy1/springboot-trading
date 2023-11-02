package de.segoy.springboottradingweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private RoleHierarchy roleHierarchy;

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased affirmativeBased = (AffirmativeBased) super.accessDecisionManager();
        affirmativeBased.getDecisionVoters().add(
                new RoleHierarchyVoter(roleHierarchy)
        );
        return affirmativeBased;
    }
    /*
    Class after RoleHierarchy is resolved to @EnableMethodSecurity
    delete RoleHierarchyConfigurationClass After
     */
//    @EnableMethodSecurity(securedEnabled = true, prePostEnabled = false)
//    public class GlobalMethodSecurityConfig {
//
//        @Bean
//        static RoleHierarchy roleHierarchy() {
//            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//            roleHierarchy.setHierarchy(
//                    new RolesHierarchyBuilder()
//                            .append(SUPER_ADMIN, CUSTOMERS_ADMIN)
//                            .append(CUSTOMERS_ADMIN, CUSTOMERS_CREATE)
//                            .append(CUSTOMERS_ADMIN, CUSTOMERS_READ)
//                            .append(CUSTOMERS_ADMIN, CUSTOMERS_DELETE)
//                            .append(CUSTOMERS_ADMIN, CUSTOMERS_PAG_VIEW)
//
//                            .append(SUPER_ADMIN, EMPLOYEES_ADMIN)
//                            .append(EMPLOYEES_ADMIN, EMPLOYEES_CREATE)
//                            .append(EMPLOYEES_ADMIN, EMPLOYEES_READ)
//                            .append(EMPLOYEES_ADMIN, EMPLOYEES_DELETE)
//                            .append(EMPLOYEES_ADMIN, EMPLOYEES_PAG_VIEW)
//
//                            .append(SUPER_ADMIN, DEPARTMENTS_ADMIN)
//                            .append(DEPARTMENTS_ADMIN, DEPARTMENTS_CREATE)
//                            .append(DEPARTMENTS_ADMIN, DEPARTMENTS_READ)
//                            .append(DEPARTMENTS_ADMIN, DEPARTMENTS_DELETE)
//                            .append(DEPARTMENTS_ADMIN, DEPARTMENTS_PAG_VIEW)
//
//                            .build()
//            );
//
//            return roleHierarchy;
//        }
//
//        @Bean
//        static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
//            DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//            expressionHandler.setRoleHierarchy(roleHierarchy);
//            return expressionHandler;
//        }
}
