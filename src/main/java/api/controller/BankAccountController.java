package api.controller;


import api.dto.BankAccountDTO;
import api.factory.BankAccountsFactory;
import entity.BankAccountEntity;
import entity.ClientEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import services.BankAccountServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BankAccountController {
    BankAccountServiceImpl bankAccountService;
    BankAccountsFactory bankAccountsFactory;

    public static final String FETCH_BANK_ACCOUNT = "/api/client/bankAccount";
    public static final String CREATE_BANK_ACCOUNT = "/api/client/bankAccount/{client_id}";
    public static final String DELETE_CLIENT = "/api/client/{id}";

    @GetMapping(FETCH_BANK_ACCOUNT)
    public ResponseEntity<List<BankAccountDTO>> fetchBankAccounts(){
        List<BankAccountEntity> bankAccountEntities = bankAccountService.getAllEntities();

        return ResponseEntity.ok(bankAccountsFactory.createBankAccountDTOList(bankAccountEntities));
    }

    @PostMapping(CREATE_BANK_ACCOUNT)
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        ClientEntity client = bankAccountDTO.getClient();
        BigDecimal balance = bankAccountDTO.getBalance();
        LocalDateTime createDate = bankAccountDTO.getCreationDate();
        bankAccountService.createEntity(BankAccountEntity.builder()
                .client(client)
                .balance(balance)
                .creationDate(createDate).build());
        return new ResponseEntity<>(bankAccountDTO, HttpStatus.CREATED);
    }
}
