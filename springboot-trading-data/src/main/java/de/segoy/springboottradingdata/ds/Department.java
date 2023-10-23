package de.segoy.springboottradingdata.ds;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static jakarta.persistence.GenerationType.IDENTITY;

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
