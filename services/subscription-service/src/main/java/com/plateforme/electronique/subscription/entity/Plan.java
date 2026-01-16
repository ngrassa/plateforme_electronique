package com.plateforme.electronique.subscription.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plans", indexes = {
    @Index(name = "idx_plan_name", columnList = "name", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, unique = true)
    private Name name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "price_monthly", precision = 10, scale = 2)
    private BigDecimal priceMonthly;

    @Column(name = "price_annual", precision = 10, scale = 2)
    private BigDecimal priceAnnual;

    @Column(name = "max_invoices_per_month")
    private Integer maxInvoicesPerMonth;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "signature_included")
    private boolean signatureIncluded;

    @Column(name = "api_access")
    private boolean apiAccess;

    public enum Name {
        FREE,
        BASIC,
        PREMIUM,
        ENTERPRISE
    }
}
