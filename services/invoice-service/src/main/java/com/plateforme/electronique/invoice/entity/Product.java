package com.plateforme.electronique.invoice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_product_sku", columnList = "sku", unique = true),
    @Index(name = "idx_product_name", columnList = "name"),
    @Index(name = "idx_product_category", columnList = "category"),
    @Index(name = "idx_product_tenant", columnList = "tenantId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name cannot exceed 200 characters")
    private String name;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;

    @Column(nullable = false, precision = 15, scale = 4)
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
    private BigDecimal unitPrice;

    @Column(length = 10)
    @NotBlank(message = "Currency is required")
    @Builder.Default
    private String currency = "EUR";

    @Column(precision = 7, scale = 2)
    @DecimalMin(value = "0.0", message = "Tax rate must be non-negative")
    @DecimalMax(value = "100.0", message = "Tax rate cannot exceed 100%")
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.valueOf(20.0); // 20% default

    @Column(length = 10)
    @Size(max = 10, message = "Unit cannot exceed 10 characters")
    @Builder.Default
    private String unit = "PCS";

    @Column(name = "tenant_id", nullable = false)
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}