package org.example.mediserve.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.example.mediserve.domain.dto.request.PaymentRequestDTO;
import org.example.mediserve.domain.entity.PaymentEntity;
import org.example.mediserve.domain.entity.UserEntity;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.exception.DataNotFoundException;
import org.example.mediserve.repository.PaymentRepository;
import org.example.mediserve.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository paymentRepository;

    private UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, @Value("${stripe.apiKey}") String secretKey, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        Stripe.apiKey = secretKey;
        this.modelMapper = modelMapper;
    }

    public PaymentIntent createPaymentIntent(PaymentRequestDTO paymentRequestDTO) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentRequestDTO.getAmount());
        params.put("currency", paymentRequestDTO.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        UserEntity userEntity = userRepository.findByUsername(paymentRequestDTO.getReceiptUsername()).orElseThrow(
                () -> new DataNotFoundException("User is not found with this username in database")
        );
        userEntity.setIsPaid(true);
        userEntity.setRole(UserRole.USER);
        userRepository.save(userEntity);
        paymentRepository.save(modelMapper.map(paymentRequestDTO, PaymentEntity.class));
        return PaymentIntent.create(params);
    }

}
