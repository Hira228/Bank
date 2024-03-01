package org.example.services;

import org.example.entity.ClientEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.repositories.ClientRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientServiceImpl implements org.example.services.Service<ClientEntity, Long> {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    ClientRepository clientRepository;
    PasswordEncoder passwordEncoder;

    public List<ClientEntity> searchClientsDate(LocalDate dateOfBirth) {
        return clientRepository.findByFilterDate(dateOfBirth);
    }

    public List<ClientEntity> searchClientsTelephoneNumber(String telephoneNumber) {
        return clientRepository.findByFilterTelephoneNumber(telephoneNumber);
    }

    public List<ClientEntity> searchClientsEmail(String email) {
        return clientRepository.findByFilterEmail(email);
    }

    public List<ClientEntity> searchClientsName(String name) {
        return clientRepository.findByFilterName(name);
    }
    @Override
    public ClientEntity createEntity(ClientEntity entity) {
        entity.setRoles("CLIENT");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        ClientEntity client = clientRepository.save(entity);
        logger.info("Client with id {} created successfully", entity.getId());
        return client;
    }

    @Override
    public Optional<ClientEntity> getEntityById(Long aLong) {
        Optional<ClientEntity> clientEntityOptional = clientRepository.findById(aLong);
        if (clientEntityOptional.isEmpty()) {
            logger.warn("Client with ID {} not found", aLong);
        }
        return clientEntityOptional;
    }

    @Override
    public void updateEntity(ClientEntity entity) {
        clientRepository.save(entity);
        logger.info("Updated client: {}", entity);
    }

    @Override
    public void deleteEntityById(Long aLong) {
        clientRepository.deleteById(aLong);
        logger.info("Deleted client with ID {}", aLong);
    }


    public Optional<ClientEntity> findByEmails(String email) {
        List<ClientEntity> clients = clientRepository.findAll();
        for (ClientEntity client : clients) {
            List<String> clientEmails = client.getEmails();
            if (clientEmails.contains(email)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }


    public Optional<ClientEntity> findByTelephoneNumbers(String telephoneNumber) {
        List<ClientEntity> clients = clientRepository.findAll();
        for (ClientEntity client : clients) {
            List<String> clientTelephoneNumbers = client.getTelephoneNumbers();
            if (clientTelephoneNumbers.contains(telephoneNumber)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ClientEntity> getAllEntities() {
        List<ClientEntity> allClients = clientRepository.findAll();
        logger.info("Retrieved {} clients", allClients.size());
        return allClients;
    }

    public Optional<ClientEntity> updateEmail(Long clientId, String updatableEmail, String newEmail) {
        Optional<ClientEntity> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            ClientEntity client = clientOptional.get();
            if (clientRepository.existsByEmailsAndIdNot(newEmail, clientId)) {
                logger.warn("Email {} is already in use by another client", newEmail);
                return Optional.empty();
            }
            int k = -1;
            for(int i = 0; i < client.getEmails().size(); ++i) {
                if(client.getEmails().get(i) != null && client.getEmails().get(i).equals(updatableEmail)) {
                    k = i;
                    break;
                }
            }
            if(k == -1){
                logger.info("Invalid replacement email client with ID {}", clientId);
                return Optional.empty();
            }
            client.getEmails().remove(k);
            client.getEmails().add(k, newEmail);
            clientRepository.save(client);
            logger.info("Updated email for client with ID {}: {}", clientId, newEmail);
            return Optional.of(client);
        } else {
            logger.warn("Client with ID {} not found", clientId);
            return Optional.empty();
        }
    }

    public Optional<ClientEntity> updateTelephoneNumber(Long clientId, String updatableTelephoneNumber, String newTelephoneNumber) {
        Optional<ClientEntity> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            ClientEntity client = clientOptional.get();
            if (clientRepository.existsByTelephoneNumbersAndIdNot(newTelephoneNumber, clientId)) {
                logger.warn("Telephone number {} is already in use by another client", newTelephoneNumber);
                return Optional.empty();
            }
            int k = -1;
            for (int i = 0; i < client.getTelephoneNumbers().size(); ++i) {
                if (client.getTelephoneNumbers().get(i) != null && client.getTelephoneNumbers().get(i).equals(updatableTelephoneNumber)) {
                    k = i;
                    break;
                }
            }
            if (k == -1) {
                logger.info("Invalid replacement telephone number for client with ID {}", clientId);
                return Optional.empty();
            }
            client.getTelephoneNumbers().remove(k);
            client.getTelephoneNumbers().add(k, newTelephoneNumber);
            clientRepository.save(client);
            logger.info("Updated telephone number for client with ID {}: {}", clientId, newTelephoneNumber);
            return Optional.of(client);
        } else {
            logger.warn("Client with ID {} not found", clientId);
            return Optional.empty();
        }
    }

}
