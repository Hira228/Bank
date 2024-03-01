package org.example.config;

import org.example.entity.ClientEntity;
import org.example.repositories.ClientRepository;
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
    private final ClientRepository clientRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public SchedulerConfig(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateBalance() {
        Iterable<ClientEntity> clients = clientRepository.findAll();
        for (ClientEntity client : clients) {
            BigDecimal internalBalance = client.getBalance();
            BigDecimal newBalance = internalBalance.multiply(BigDecimal.valueOf(1.05));
            if (newBalance.compareTo(internalBalance.multiply(BigDecimal.valueOf(2.07))) < 0) {
                client.setBalance(newBalance);
            }
        }
        clientRepository.saveAll(clients);
    }
}
