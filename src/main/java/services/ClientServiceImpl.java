package services;

import entity.ClientEntity;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientServiceImpl implements services.Service<ClientEntity, Long> {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    ClientRepository clientRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public ClientEntity createEntity(ClientEntity entity) {
        if(clientRepository.findById(entity.getId()).isPresent()) {
            logger.warn("Client with id {} already exists", entity.getId());
        }
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

    public Optional<ClientEntity> getEntityByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<ClientEntity> getEntityByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    public Optional<ClientEntity> getEntityByTelephoneNumber(String telephoneNumber) {
        return clientRepository.findByTelephoneNumber(telephoneNumber);
    }

    @Override
    public List<ClientEntity> getAllEntities() {
        List<ClientEntity> allClients = clientRepository.findAll();
        logger.info("Retrieved {} clients", allClients.size());
        return allClients;
    }
}
