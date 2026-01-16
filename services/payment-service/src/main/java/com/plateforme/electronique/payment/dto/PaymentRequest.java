package com.plateforme.electronique.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequest {
    @NotNull
    private UUID invoiceId;

    @NotNull
    private UUID userId;

    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    private String currency = "TND";
    private String method = "CARD";
}
