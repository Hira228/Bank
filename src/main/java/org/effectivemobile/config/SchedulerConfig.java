package org.effectivemobile.config;

import org.effectivemobile.entity.BankAccountEntity;
import org.effectivemobile.repositories.BankAccountRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    private final BankAccountRepository bankAccountRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public SchedulerConfig(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalance() {
        Iterable<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
        for (BankAccountEntity bankAccount : bankAccountEntities) {
            BigDecimal internalBalance = bankAccount.getAccountBalance();
            BigDecimal newBalance = internalBalance.multiply(BigDecimal.valueOf(1.05));
            if (newBalance.compareTo(bankAccount.getClient().getInitialBalance().multiply(BigDecimal.valueOf(2.07))) < 0) {
                bankAccount.setAccountBalance(newBalance);
            }
        }
        bankAccountRepository.saveAll(bankAccountEntities);
    }
}
