package org.example.api.controller;


import org.example.api.dto.AckDTO;
import org.example.api.dto.BankAccountDTO;
import org.example.api.dto.ClientDTO;
import org.example.api.factory.BankAccountsFactory;
import org.example.entity.BankAccountEntity;
import org.example.entity.ClientEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.services.BankAccountServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.services.ClientServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientAndBankAccountController {
    ClientServiceImpl clientService;
    BankAccountServiceImpl bankAccountService;
    BankAccountsFactory bankAccountsFactory;
    public static final String FETCH_BANK_ACCOUNTS = "/org/example/api/client/";
    public static final String CREATE_BANK_ACCOUNT = "/org/example/api/client/registration";
    public static final String DELETE_BANK_ACCOUNT = "/org/example/api/client/{clientId}";
    public static final String CHANGE_TELEPHONE_NUMBER = "/org/example/api/client/{clientId}/telephoneNumber/{oldTelephoneNumber}";
    public static final String CHANGE_EMAIL_ADDRESS = "/org/example/api/client/{clientId}/email/{oldEmailAddress}";
    public static final String DELETE_EMAIL_ADDRESS = "/org/example/api/client/{clientId}/email/{delEmailAddress}";
    public static final String DELETE_TELEPHONE_NUMBER = "/org/example/api/client/{clientId}/telephoneNumber/{delTelephoneNumber}";
    public static final String SEARCH = "/org/example/api/search";

    @GetMapping(FETCH_BANK_ACCOUNTS)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<BankAccountDTO>> fetchClients() {
        List<BankAccountEntity> accounts = bankAccountService.getAllEntities();

        return ResponseEntity.ok(bankAccountsFactory.createBankAccountDTOList(accounts));
    }

    @GetMapping(SEARCH)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ClientEntity>> searchClients(@RequestParam(required = false) LocalDate dateOfBirth,
                                                            @RequestParam(required = false) String telephoneNumber,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String email) {
        List<ClientEntity> clientEntities = new ArrayList<>();
        if(dateOfBirth != null) {
            clientEntities = clientService.searchClientsDate(dateOfBirth);
        } else if(telephoneNumber != null) {
            clientEntities = clientService.searchClientsTelephoneNumber(telephoneNumber);
        } else if(name != null) {
            clientEntities = clientService.searchClientsName(name);
        } else if(email != null) {
            clientEntities = clientService.searchClientsEmail(email);
        }

        return ResponseEntity.ok(clientEntities);
    }
    @PostMapping(CREATE_BANK_ACCOUNT)
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody ClientDTO clientRequest) {
        String name = clientRequest.getName();
        String password = clientRequest.getPassword();
        List<String> emails = clientRequest.getEmails();
        List<String> telephoneNumbers = clientRequest.getTelephoneNumbers();
        BigDecimal balance = clientRequest.getBalance();
        LocalDate dateOfBirth = clientRequest.getDateOfBirth();

        if (!isValidClientData(password, emails, telephoneNumbers, balance)) {
            return ResponseEntity.badRequest().build();
        }

        ClientEntity client = clientService.createEntity(ClientEntity.makeDefault(name,
                password, balance, emails, telephoneNumbers, dateOfBirth));

        BankAccountEntity bankAccount = bankAccountService.createEntity(BankAccountEntity.builder()
                .client(client)
                .balance(balance)
                .creationDate(LocalDateTime.now())
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountsFactory.createBankAccountDTO(bankAccount));
    }

    @DeleteMapping(DELETE_BANK_ACCOUNT)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteBankAccount(@PathVariable Long clientId) {
        if(clientService.getEntityById(clientId).isPresent() && bankAccountService.getEntityById(clientId).isPresent()) {
            bankAccountService.deleteEntityById(clientId);
            clientService.deleteEntityById(clientId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(true));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
    }

    @DeleteMapping(DELETE_EMAIL_ADDRESS)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteEmail(@PathVariable Long clientId,
                                              @PathVariable String delEmailAddress) {
        Optional<ClientEntity> client = clientService.getEntityById(clientId);
        if(client.isPresent() && bankAccountService.getEntityById(clientId).isPresent()
                && client.get().getEmails().size() > 1) {
            boolean result = client.get().removeEmail(delEmailAddress);
            if(result)clientService.updateEntity(client.get());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(result));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
    }

    @DeleteMapping(DELETE_TELEPHONE_NUMBER)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteTelephoneNumber(@PathVariable Long clientId,
                                              @PathVariable String delTelephoneNumber) {
        Optional<ClientEntity> client = clientService.getEntityById(clientId);
        if(client.isPresent() && bankAccountService.getEntityById(clientId).isPresent()
                && client.get().getTelephoneNumbers().size() > 1) {
            boolean result = client.get().removeTelephoneNumber(delTelephoneNumber);
            if(result) clientService.updateEntity(client.get());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(result));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
    }

    @PutMapping(CHANGE_TELEPHONE_NUMBER + "/{newTelephoneNumber}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> updateTelephoneNumber(@PathVariable Long clientId,
                                                        @PathVariable String oldTelephoneNumber,
                                                        @PathVariable String newTelephoneNumber) {
        Optional<ClientEntity> client = clientService.getEntityById(clientId);
        if(client.isPresent()
                && bankAccountService.getEntityById(clientId).isPresent()
                    && clientService.findByTelephoneNumbers(newTelephoneNumber).isEmpty()) {
            boolean result = client.get().checkTelephoneNumbers(oldTelephoneNumber);
            if(result) clientService.updateTelephoneNumber(clientId, oldTelephoneNumber, newTelephoneNumber);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(result));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
    }

    @PutMapping(CHANGE_EMAIL_ADDRESS + "/{newEmailAddress}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> updateEmailAddress(@PathVariable Long clientId,
                                                     @PathVariable String oldEmailAddress,
                                                     @PathVariable String newEmailAddress) {
        Optional<ClientEntity> client = clientService.getEntityById(clientId);
        if(client.isPresent()
                && bankAccountService.getEntityById(clientId).isPresent()
                && clientService.findByEmails(newEmailAddress).isEmpty()) {
            boolean result = client.get().checkEmail(oldEmailAddress);
            if(result) clientService.updateEmail(clientId, oldEmailAddress, newEmailAddress);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(result));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
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

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }
}
