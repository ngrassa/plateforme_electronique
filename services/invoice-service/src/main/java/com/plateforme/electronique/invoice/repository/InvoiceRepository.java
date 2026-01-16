package com.plateforme.electronique.invoice.repository;

import com.plateforme.electronique.invoice.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findByOwnerUserId(UUID ownerUserId, Pageable pageable);
    Optional<Invoice> findByIdAndOwnerUserId(UUID id, UUID ownerUserId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
