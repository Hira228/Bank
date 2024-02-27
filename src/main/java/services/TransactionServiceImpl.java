package services;

import entity.TransactionEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements services.Service<TransactionEntity, UUID> {
    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean createEntity(TransactionEntity entity) {
        transactionRepository.save(entity);
        logger.info("Transaction created: {}", entity);
        return true;
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
