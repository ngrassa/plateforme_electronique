package com.plateforme.electronique.invoice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateInvoiceRequest {

    @NotNull
    private UUID ownerUserId;

    @NotBlank
    private String clientName;

    @NotBlank
    private String clientEmail;

    private String billingAddress;

    private BigDecimal vatRate;

    private LocalDate issueDate;
    private LocalDate dueDate;

    @NotEmpty
    @Valid
    private List<CreateInvoiceItemRequest> items;
}
