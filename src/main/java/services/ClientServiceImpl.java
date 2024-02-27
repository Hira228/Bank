package services;

import entity.ClientEntity;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements services.Service<ClientEntity, Long> {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public boolean createEntity(ClientEntity entity) {
        if(clientRepository.findById(entity.getId()).isPresent()) {
            logger.warn("Client with id {} already exists", entity.getId());
            return false;
        }
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        clientRepository.save(entity);
        logger.info("Client with id {} created successfully", entity.getId());
        return true;
    }

    @Override
    @Transactional
    public Optional<ClientEntity> getEntityById(Long aLong) {
        Optional<ClientEntity> clientEntityOptional = clientRepository.findById(aLong);
        if (clientEntityOptional.isEmpty()) {
            logger.warn("Client with ID {} not found", aLong);
        }
        return clientEntityOptional;
    }

    @Override
    @Transactional
    public void updateEntity(ClientEntity entity) {
        clientRepository.save(entity);
        logger.info("Updated client: {}", entity);
    }

    @Override
    @Transactional
    public void deleteEntityById(Long aLong) {
        clientRepository.deleteById(aLong);
        logger.info("Deleted client with ID {}", aLong);
    }

    @Transactional
    public Optional<ClientEntity> getEntityByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Transactional
    public Optional<ClientEntity> getEntityByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Transactional
    public Optional<ClientEntity> getEntityByTelephoneNumber(String telephoneNumber) {
        return clientRepository.findByTelephoneNumber(telephoneNumber);
    }

    @Override
    @Transactional
    public List<ClientEntity> getAllEntities() {
        List<ClientEntity> allClients = clientRepository.findAll();
        logger.info("Retrieved {} clients", allClients.size());
        return allClients;
    }
}
