package org.example.services;

import org.apache.logging.log4j.LogManager;
import org.example.api.exceptions.ClientNotFoundException;
import org.example.api.exceptions.InsufficientFundsException;
import org.example.entity.BankAccountEntity;
import org.example.entity.ClientEntity;
import org.example.entity.TransactionEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.Logger;
import org.example.repositories.BankAccountRepository;
import org.example.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.example.repositories.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionServiceImpl implements org.example.services.Service<TransactionEntity, UUID> {
    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
    TransactionRepository transactionRepository;
    BankAccountRepository bankAccountRepository;
    @Transactional
    public void transferMoney(Long senderAccountId, Long receiverAccountId, BigDecimal amount) {
        BankAccountEntity senderAccount = bankAccountRepository.findById(senderAccountId)
                .orElseThrow(() -> new ClientNotFoundException("Sender account not found"));
        BankAccountEntity receiverAccount = bankAccountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new ClientNotFoundException("Receiver account not found"));

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Sender account has insufficient funds");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(receiverAccount);

        UUID uuid = UUID.randomUUID();

        LocalDateTime dateTime = LocalDateTime.now();

        TransactionEntity transaction =
                new TransactionEntity(uuid, senderAccount, receiverAccount, amount, dateTime);

        transactionRepository.save(transaction);

        logger.info("Money transfer from account {} to account {} in the amount of {} completed successfully",
                senderAccountId, receiverAccountId, amount);
    }

    @Override
    public TransactionEntity createEntity(TransactionEntity entity) {
        TransactionEntity transactionEntity = transactionRepository.save(entity);
        logger.info("Transaction created: {}", entity);
        return transactionEntity;
    }

    @Override
    public Optional<TransactionEntity> getEntityById(UUID uuid) {
        Optional<TransactionEntity> transactionEntityOptional = transactionRepository.findById(uuid);
        if (transactionEntityOptional.isEmpty()) {
            logger.warn("Transaction with ID {} not found", uuid);
        }
        return transactionEntityOptional;
    }
    @Override
    public void updateEntity(TransactionEntity entity) {
        transactionRepository.save(entity);
        logger.info("Updated transaction: {}", entity);
    }

    @Override
    public void deleteEntityById(UUID uuid) {
        transactionRepository.deleteById(uuid);
        logger.info("Deleted transaction with ID {}", uuid);
    }

    @Override
    public List<TransactionEntity> getAllEntities() {
        List<TransactionEntity> allTransactions = transactionRepository.findAll();
        logger.info("Retrieved {} transactions", allTransactions.size());
        return allTransactions;
    }
}
