package com.plateforme.electronique.invoice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_items", indexes = {
    @Index(name = "idx_invoice_item_invoice", columnList = "invoiceId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class InvoiceItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @NotNull(message = "Invoice is required")
    @JsonBackReference
    private Invoice invoice;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false, precision = 10, scale = 3)
    @DecimalMin(value = "0.001", message = "Quantity must be positive")
    private BigDecimal quantity;

    @Column(nullable = false, precision = 15, scale = 4)
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
    private BigDecimal unitPrice;

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.valueOf(19);

    @Column(name = "line_total_ht", precision = 15, scale = 4)
    private BigDecimal lineTotalHt;

    @Transient
    public BigDecimal computeLineTotalHt() {
        return unitPrice.multiply(quantity).setScale(4, java.math.RoundingMode.HALF_UP);
    }
}
