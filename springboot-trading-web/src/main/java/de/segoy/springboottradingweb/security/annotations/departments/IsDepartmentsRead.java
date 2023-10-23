package com.spring.professional.exam.tutorial.module06.question01.security.annotations.departments;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.spring.professional.exam.tutorial.module06.question01.security.SecurityRoles.DEPARTMENTS_READ;
import static com.spring.professional.exam.tutorial.module06.question01.security.SecurityRoles.ROLE_PREFIX;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Secured(ROLE_PREFIX + DEPARTMENTS_READ)
public @interface IsDepartmentsRead {
}
