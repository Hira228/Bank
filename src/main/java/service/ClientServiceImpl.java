package service;

import entity.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import repositories.ClientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements service.Service<ClientEntity, Long> {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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

    @Override
    public List<ClientEntity> getAllEntities() {
        List<ClientEntity> allClients = clientRepository.findAll();
        logger.info("Retrieved {} clients", allClients.size());
        return allClients;
    }
}
