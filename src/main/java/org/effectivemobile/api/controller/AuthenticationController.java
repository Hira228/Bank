package org.effectivemobile.api.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.effectivemobile.api.dto.AuthenticateDTO;
import org.effectivemobile.api.dto.AuthenticationRequest;
import org.effectivemobile.api.dto.ClientDTO;
import org.effectivemobile.config.ClientDetails;
import org.effectivemobile.config.JwtService;
import org.effectivemobile.entity.BankAccountEntity;
import org.effectivemobile.entity.ClientEntity;
import org.effectivemobile.services.BankAccountServiceImpl;
import org.effectivemobile.services.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    ClientServiceImpl clientService;
    BankAccountServiceImpl bankAccountService;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    public static final String CREATE_BANK_ACCOUNT = "/registration";

    public static final String AUTHENTICATE = "/authenticate";

    @PostMapping(CREATE_BANK_ACCOUNT)
    public ResponseEntity<AuthenticateDTO> createBankAccount(@Valid @RequestBody ClientDTO clientRequest) {
        if(clientRequest == null) return ResponseEntity.badRequest().build();
        String name = clientRequest.getName();
        String password = clientRequest.getPassword();
        List<String> emails = clientRequest.getEmails();
        List<String> telephoneNumbers = clientRequest.getTelephoneNumbers();
        BigDecimal balance = clientRequest.getInitialBalance();
        LocalDate dateOfBirth = clientRequest.getDateOfBirth();

        if (!isValidClientData(password, emails, telephoneNumbers, balance)) {
            return ResponseEntity.badRequest().build();
        }

        ClientEntity client = clientService.createEntity(ClientEntity.makeDefault(name,
                password, balance, emails, telephoneNumbers, dateOfBirth));

        String jwtToken = jwtService.generateToken(new ClientDetails(client));

        BankAccountEntity bankAccount = bankAccountService.createEntity(BankAccountEntity.builder()
                .client(client)
                .accountBalance(balance)
                .creationDate(LocalDateTime.now())
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticateDTO.builder()
                .token(jwtToken)
                .build());
    }

    @PostMapping(AUTHENTICATE)
    public ResponseEntity<AuthenticateDTO> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                        request.getPassword()
                )
        );
        ClientEntity client = clientService.findByEmails(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(new ClientDetails(client));
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthenticateDTO.builder()
                .token(jwtToken)
                .build());
    }

    private boolean isValidClientData(String password, List<String> emails, List<String> telephoneNumbers, BigDecimal balance) {
        for (String email : emails) {
            if (clientService.findByEmails(email).isPresent()) {
                return false;
            }
        }
        for (String phoneNumber : telephoneNumbers) {
            if (clientService.findByTelephoneNumbers(phoneNumber).isPresent()) {
                return false;
            }
        }

        return balance.compareTo(BigDecimal.ZERO) >= 0;
    }
}
