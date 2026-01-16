package com.plateforme.electronique.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateInvoiceItemRequest {

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.001")
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0")
    private BigDecimal taxRate;
}
