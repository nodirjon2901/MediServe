package org.example.mediserve.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.dto.request.PaymentRequestDTO;
import org.example.mediserve.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            description = "This API is used to create a payment intent",
            method = "POST method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"Any role"})
    )
    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequestDTO) throws StripeException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentRequestDTO);
        String paymentIntentJson = paymentIntent.toJson();
        return ResponseEntity.ok(paymentIntentJson);
    }


}
