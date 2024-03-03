package org.effectivemobile.services;

import org.effectivemobile.api.exceptions.ClientNotFoundException;
import org.effectivemobile.config.ClientDetails;
import org.effectivemobile.entity.BankAccountEntity;
import org.effectivemobile.repositories.BankAccountRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.effectivemobile.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BankAccountServiceImpl implements org.effectivemobile.services.Service<BankAccountEntity, Long> {
    private static final Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);

    BankAccountRepository bankAccountRepository;
    ClientRepository clientRepository;

    @Override
    public BankAccountEntity createEntity(BankAccountEntity entity) {
        BankAccountEntity bankAccountEntity = bankAccountRepository.save(entity);
        logger.info("Bank account created: {}", entity);
        return bankAccountEntity;
    }

    @Override
    public Optional<BankAccountEntity> getEntityById(Long aLong) {
        Optional<BankAccountEntity> bankAccountEntityOptional = bankAccountRepository.findById(aLong);
        if (bankAccountEntityOptional.isEmpty()) {
            logger.warn("Bank account with ID {} not found", aLong);
        }
        return bankAccountEntityOptional;
    }

    @Override
    public void updateEntity(BankAccountEntity entity) {
        bankAccountRepository.save(entity);
        logger.info("Updated bank account: {}", entity);
    }

    @Override
    public void deleteEntityById(Long aLong) {
        if(bankAccountRepository.findById(aLong).isEmpty() && clientRepository.findById(aLong).isEmpty()) throw new ClientNotFoundException("Not found");
        bankAccountRepository.deleteById(aLong);
        clientRepository.deleteById(aLong);
        logger.info("Deleted bank account with ID {}", aLong);
    }

    public void deleteEntity() {
        deleteEntityById(getId());
    }

    private Long getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getId();
    }

    @Override
    public List<BankAccountEntity> getAllEntities() {
        List<BankAccountEntity> allBankAccounts = bankAccountRepository.findAll();
        logger.info("Retrieved {} bank accounts", allBankAccounts.size());
        return allBankAccounts;
    }
}
