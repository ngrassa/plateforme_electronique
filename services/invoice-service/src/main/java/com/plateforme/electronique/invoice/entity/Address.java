package com.plateforme.electronique.invoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(length = 200)
    @Size(max = 200, message = "Street address cannot exceed 200 characters")
    private String street;

    @Column(length = 10)
    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$", message = "Invalid postal code format")
    private String postalCode;

    @Column(length = 100)
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String city;

    @Column(length = 100)
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    @Builder.Default
    private String country = "France";

    @Column(length = 500)
    @Size(max = 500, message = "Additional information cannot exceed 500 characters")
    private String additionalInfo;
}