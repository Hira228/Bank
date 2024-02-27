package service;

import entity.BankAccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BankAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements service.Service<BankAccountEntity, Long> {
    private static final Logger logger = LogManager.getLogger(BankAccountServiceImpl.class);
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public boolean createEntity(BankAccountEntity entity) {
        bankAccountRepository.save(entity);
        logger.info("Bank account created: {}", entity);
        return true;
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
