package org.effectivemobile.api.controller;


import org.effectivemobile.api.dto.*;
import org.effectivemobile.api.exceptions.ClientNotFoundException;
import org.effectivemobile.api.factory.BankAccountsFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.effectivemobile.services.BankAccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.effectivemobile.services.ClientServiceImpl;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/client")
public class ClientAndBankAccountController {
    ClientServiceImpl clientService;
    BankAccountServiceImpl bankAccountService;
    BankAccountsFactory bankAccountsFactory;

    public static final String FETCH_BANK_ACCOUNTS = "/";
    public static final String DELETE_BANK_ACCOUNT = "/deleteBankAccount/";
    public static final String CHANGE_EMAIL_ADDRESS = "/email/changeEmail";
    public static final String DELETE_EMAIL_ADDRESS = "/email/delete";
    public static final String ADD_NEW_EMAIL_ADDRESS = "/email/addNew";
    public static final String DELETE_TELEPHONE_NUMBER = "/telephoneNumber/delete";
    public static final String CHANGE_TELEPHONE_NUMBER = "/telephoneNumber/changeTelephoneNumber";
    public static final String ADD_NEW_TELEPHONE_NUMBER = "/telephoneNumber/addNewTelephoneNumber";
    public static final String SEARCH = "/search";


    @GetMapping(FETCH_BANK_ACCOUNTS)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<BankAccountDTO>> fetchClients() {
        return ResponseEntity.ok(bankAccountsFactory.createBankAccountDTOList(bankAccountService.getAllEntities()));
    }

    @GetMapping(SEARCH)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<BankAccountDTO>> searchClients(
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String telephoneNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        return ResponseEntity.ok(clientService.searchClients(dateOfBirth, telephoneNumber, name, email));
    }


    @DeleteMapping(DELETE_BANK_ACCOUNT)
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteBankAccount() {
        try {
            bankAccountService.deleteEntity();
            return ResponseEntity.ok(new AckDTO(true));
        }catch (ClientNotFoundException err) {
            return ResponseEntity.ok(new AckDTO(false));
        }
    }

    @DeleteMapping(DELETE_EMAIL_ADDRESS + "/{delEmailAddress}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteEmail(@PathVariable String delEmailAddress) {

        boolean result = clientService.removeEmail(delEmailAddress);

        return ResponseEntity.ok(new AckDTO(result));
    }

    @DeleteMapping(DELETE_TELEPHONE_NUMBER + "/{delTelephoneNumber}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> deleteTelephoneNumber(@PathVariable String delTelephoneNumber) {

        boolean result = clientService.removeTelephoneNumber(delTelephoneNumber);

        return ResponseEntity.ok(new AckDTO(result));
    }

    @PutMapping(CHANGE_TELEPHONE_NUMBER + "/{oldTelephoneNumber}/{newTelephoneNumber}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> updateTelephoneNumber(@PathVariable String oldTelephoneNumber,
                                                        @PathVariable String newTelephoneNumber) {

        boolean result = clientService.updateTelephoneNumber(oldTelephoneNumber, newTelephoneNumber);

        return ResponseEntity.ok(new AckDTO(result));
    }

    @PutMapping(CHANGE_EMAIL_ADDRESS + "/{oldEmailAddress}/{newEmailAddress}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> updateEmailAddress(@PathVariable String oldEmailAddress,
                                                     @PathVariable String newEmailAddress) {

        boolean result = clientService.updateEmail(oldEmailAddress, newEmailAddress);

        return ResponseEntity.ok().body(new AckDTO(result));
    }

    @PutMapping(ADD_NEW_EMAIL_ADDRESS + "/{newEmailAddress}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> addNewEmailAddress(@PathVariable String newEmailAddress) {
        boolean result = clientService.addNewEmailAddress(newEmailAddress);

        return ResponseEntity.ok().body(new AckDTO(result));
    }

    @PutMapping(ADD_NEW_TELEPHONE_NUMBER + "/{newTelephoneNumber}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<AckDTO> addNewTelephoneNumber(@PathVariable String newTelephoneNumber) {
        boolean result = clientService.addNewTelephoneNumber(newTelephoneNumber);

        return ResponseEntity.ok().body(new AckDTO(result));
    }
}
