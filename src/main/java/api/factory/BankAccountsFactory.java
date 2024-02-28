package api.factory;

import api.dto.BankAccountDTO;
import api.dto.ClientDTO;
import entity.BankAccountEntity;
import entity.ClientEntity;
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
