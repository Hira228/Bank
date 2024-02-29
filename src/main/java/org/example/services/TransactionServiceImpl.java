package org.example.services;

import org.apache.logging.log4j.LogManager;
import org.example.entity.TransactionEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.example.repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionServiceImpl implements org.example.services.Service<TransactionEntity, UUID> {
    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
    TransactionRepository transactionRepository;

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
