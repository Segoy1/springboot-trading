package de.segoy.springboottradingweb.security.annotations.employees;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static de.segoy.springboottradingweb.security.SecurityRoles.EMPLOYEES_ADMIN;
import static de.segoy.springboottradingweb.security.SecurityRoles.ROLE_PREFIX;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(ROLE_PREFIX + EMPLOYEES_ADMIN)
public @interface IsEmployeesAdmin {
}
