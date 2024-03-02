package org.effectivemobile.api.factory;

import org.effectivemobile.entity.BankAccountEntity;
import org.effectivemobile.api.dto.BankAccountDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankAccountsFactory {

    public BankAccountDTO createBankAccountDTO(BankAccountEntity entity) {
        return BankAccountDTO.builder().id(entity.getId())
                .client(entity.getClient())
                .accountBalance(entity.getAccountBalance())
                .creationDate(entity.getCreationDate()).build();
    }

    public List<BankAccountDTO> createBankAccountDTOList(List<BankAccountEntity> entities) {
        return entities
                .stream()
                .map(this::createBankAccountDTO)
                .collect(Collectors.toList());
    }
}
