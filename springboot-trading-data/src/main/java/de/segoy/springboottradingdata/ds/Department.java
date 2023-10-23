package com.spring.professional.exam.tutorial.module06.question01.ds;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @NotBlank
    @Size(min = 2, max = 5, message = "Code must have length of 2 - 5 characters")
    @Pattern(regexp = "[A-Za-z]*", message = "Code contains illegal characters")
    private String code;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ]*", message = "Name contains illegal characters")
    private String name;
    @NotBlank
    @Pattern(regexp = "[A-Za-z ]*", message = "Country contains illegal characters")
    private String country;

    @SuppressWarnings("unused")
    public Department() {
    }
}
