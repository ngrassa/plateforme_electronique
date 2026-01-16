package com.plateforme.electronique.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payment_invoice", columnList = "invoiceId"),
    @Index(name = "idx_payment_reference", columnList = "reference", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 30)
    private String reference;

    @Column(name = "invoice_id", nullable = false)
    @NotNull
    private UUID invoiceId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private UUID userId;

    @Column(nullable = false, precision = 15, scale = 4)
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "TND";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Method method = Method.CARD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDING;

    @Column(name = "external_transaction_id", length = 100)
    private String externalTransactionId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Method {
        CARD,
        BANK_TRANSFER,
        CASH,
        CHECK
    }

    public enum Status {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }
}
