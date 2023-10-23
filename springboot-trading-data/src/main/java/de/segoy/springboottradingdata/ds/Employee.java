package de.segoy.springboottradingdata.ds;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @NotBlank(message = "First name cannot be empty")
    @Pattern(regexp = "[A-Za-z-']*", message = "First name contains illegal characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "[A-Za-z-']*", message = "Last name contains illegal characters")
    private String lastName;
    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "[0-9\\-+]*", message = "Phone number contains illegal characters")
    private String phoneNumber;
    @NotBlank(message = "Address cannot be empty")
    @Pattern(regexp = "[\\w .\\-/,]*", message = "Address contains illegal characters")
    private String address;
    @NotBlank(message = "Cubicle No cannot be empty")
    @Pattern(regexp = "[A-Za-z0-9\\-]*", message = "Cubicle No contains illegal characters")
    private String cubicleNo;

    @SuppressWarnings("unused")
    public Employee() {
    }
}
