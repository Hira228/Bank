package org.example.services;

import org.example.entity.BankAccountEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.example.repositories.BankAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BankAccountServiceImpl implements org.example.services.Service<BankAccountEntity, Long> {
    private static final Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);

    BankAccountRepository bankAccountRepository;

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
        bankAccountRepository.deleteById(aLong);
        logger.info("Deleted bank account with ID {}", aLong);
    }

    @Override
    public List<BankAccountEntity> getAllEntities() {
        List<BankAccountEntity> allBankAccounts = bankAccountRepository.findAll();
        logger.info("Retrieved {} bank accounts", allBankAccounts.size());
        return allBankAccounts;
    }
}
