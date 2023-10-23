package com.spring.professional.exam.tutorial.module06.question01.security.annotations.customers;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.spring.professional.exam.tutorial.module06.question01.security.SecurityRoles.CUSTOMERS_ADMIN;
import static com.spring.professional.exam.tutorial.module06.question01.security.SecurityRoles.ROLE_PREFIX;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(ROLE_PREFIX + CUSTOMERS_ADMIN)
public @interface IsCustomersAdmin {
}
