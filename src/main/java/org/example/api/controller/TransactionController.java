package org.example.api.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.api.dto.AckDTO;
import org.example.api.exceptions.ClientNotFoundException;
import org.example.api.exceptions.InsufficientFundsException;
import org.example.config.ClientDetails;
import org.example.services.TransactionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;


@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionController {
    TransactionServiceImpl transactionService;
    public static final String CREATE_TRANSACTION = "/org/example/api/client/transaction/{id_recipient}/{amount}";

    @GetMapping(CREATE_TRANSACTION)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> createTransaction(@PathVariable("id_recipient") Long idRecipient,
                                                    @PathVariable("amount") BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long senderId = getUserIdFromAuthentication(authentication);
        try {
            transactionService.transferMoney(senderId, idRecipient, amount);
            return ResponseEntity.ok(new AckDTO(true));
        } catch (ClientNotFoundException | InsufficientFundsException e) {
            e.getStackTrace();
            return ResponseEntity.ok(new AckDTO(false));
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getId();
    }
}
