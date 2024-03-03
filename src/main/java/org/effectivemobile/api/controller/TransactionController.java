package org.effectivemobile.api.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.effectivemobile.api.dto.AckDTO;
import org.effectivemobile.api.exceptions.ClientNotFoundException;
import org.effectivemobile.api.exceptions.InsufficientFundsException;
import org.effectivemobile.config.ClientDetails;
import org.effectivemobile.services.TransactionServiceImpl;
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
    public static final String CREATE_TRANSACTION = "api/client/transaction/{id_recipient}/{amount}";

    @GetMapping(CREATE_TRANSACTION)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> createTransaction(@PathVariable("id_recipient") Long idRecipient,
                                                    @PathVariable("amount") BigDecimal amount) {
        try {
            transactionService.transferMoney(idRecipient, amount);
            return ResponseEntity.ok(new AckDTO(true));
        } catch (ClientNotFoundException | InsufficientFundsException e) {
            e.getStackTrace();
            return ResponseEntity.ok(new AckDTO(false));
        }
    }
}
