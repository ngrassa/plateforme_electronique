package com.plateforme.electronique.payment.controller;

import com.plateforme.electronique.payment.dto.PaymentRequest;
import com.plateforme.electronique.payment.entity.Payment;
import com.plateforme.electronique.payment.repository.PaymentRepository;
import com.plateforme.electronique.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> list() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> detail(@PathVariable UUID id) {
        return ResponseEntity.of(paymentRepository.findById(id));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> byInvoice(@PathVariable UUID invoiceId) {
        return ResponseEntity.ok(paymentRepository.findByInvoiceId(invoiceId));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Payment> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.confirm(id));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refund(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.refund(id));
    }
}
