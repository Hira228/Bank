package org.effectivemobile.api.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.effectivemobile.api.dto.AuthenticateDTO;
import org.effectivemobile.api.dto.AuthenticationRequest;
import org.effectivemobile.api.dto.ClientDTO;
import org.effectivemobile.services.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {

    ClientServiceImpl clientService;

    public static final String CREATE_BANK_ACCOUNT = "/registration";
    public static final String AUTHENTICATE = "/authenticate";

    @PostMapping(CREATE_BANK_ACCOUNT)
    public ResponseEntity<AuthenticateDTO> createBankAccount(@Valid @RequestBody ClientDTO clientRequest) {
        AuthenticateDTO authenticateDTO;
        try {
            authenticateDTO = clientService.registerClient(clientRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AuthenticateDTO(""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticateDTO);
    }

    @PostMapping(AUTHENTICATE)
    public ResponseEntity<AuthenticateDTO> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticateDTO authenticateDTO;
        try {
            authenticateDTO = clientService.authenticateClient(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticateDTO);
        }catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}