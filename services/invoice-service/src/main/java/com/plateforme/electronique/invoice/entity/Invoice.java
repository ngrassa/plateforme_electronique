package com.plateforme.electronique.invoice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoice_number", columnList = "invoiceNumber"),
    @Index(name = "idx_invoice_status", columnList = "status"),
    @Index(name = "idx_invoice_owner", columnList = "ownerUserId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Invoice {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "invoice_number", length = 30)
    private String invoiceNumber;

    @Column(name = "owner_user_id", nullable = false)
    @NotNull
    private UUID ownerUserId;

    @Column(name = "client_name", length = 255)
    private String clientName;

    @Column(name = "client_email", length = 255)
    private String clientEmail;

    @Column(name = "billing_address", length = 500)
    private String billingAddress;

    @Column(name = "subtotal_ht", precision = 15, scale = 4)
    private BigDecimal subtotalHt;

    @Column(name = "vat_rate", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal vatRate = BigDecimal.valueOf(19);

    @Column(name = "vat_amount", precision = 15, scale = 4)
    private BigDecimal vatAmount;

    @Column(name = "total_ttc", precision = 15, scale = 4)
    private BigDecimal totalTtc;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Builder.Default
    private Status status = Status.DRAFT;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "signature_hash", length = 255)
    private String signatureHash;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceItem> items;

    public enum Status {
        DRAFT,
        VALIDATED,
        SENT,
        PAID,
        CANCELLED
    }
}
