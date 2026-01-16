package com.plateforme.electronique.payment.service;

import com.plateforme.electronique.payment.dto.PaymentRequest;
import com.plateforme.electronique.payment.entity.Payment;
import com.plateforme.electronique.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment create(PaymentRequest request) {
        Payment payment = Payment.builder()
                .reference(generateReference())
                .invoiceId(request.getInvoiceId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .method(Payment.Method.valueOf(request.getMethod()))
                .status(Payment.Status.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    public Payment confirm(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus(Payment.Status.COMPLETED);
        payment.setPaymentDate(LocalDate.now());
        return paymentRepository.save(payment);
    }

    public Payment refund(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus(Payment.Status.REFUNDED);
        return paymentRepository.save(payment);
    }

    private String generateReference() {
        int year = LocalDate.now().getYear();
        long seq = paymentRepository.count() + 1;
        return String.format("PAY-%d-%05d", year, seq);
    }
}
