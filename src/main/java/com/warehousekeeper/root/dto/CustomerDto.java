package com.warehousekeeper.root.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
public class CustomerDto {
    /**
     * This is String field where store full name
     */
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String fullName;
    /**
     * This is int field where store year of birth
     */
    @NotNull(message = "Year of birth should not be empty")
    @Min(value = 1951, message = "Year of birth should be greater than 1951")
    private int yearOfBirth;
    /**
     * This is int field where store phone number
     */
    @NotNull(message = "Year of birth should not be empty")
    private int phoneNumber;
    /**
     * This is int field where store email
     */
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "You should write valid email")
    private String email;
}
