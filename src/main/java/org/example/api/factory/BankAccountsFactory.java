package org.example.api.factory;

import org.example.api.dto.BankAccountDTO;
import org.example.entity.BankAccountEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankAccountsFactory {

    public BankAccountDTO createBankAccountDTO(BankAccountEntity entity) {
        return BankAccountDTO.builder().id(entity.getId())
                .client(entity.getClient())
                .balance(entity.getBalance())
                .creationDate(entity.getCreationDate()).build();
    }

    public List<BankAccountDTO> createBankAccountDTOList(List<BankAccountEntity> entities) {
        return entities
                .stream()
                .map(this::createBankAccountDTO)
                .collect(Collectors.toList());
    }
}
