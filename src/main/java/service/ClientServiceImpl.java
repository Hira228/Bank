package service;

import entity.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ClientRepository;

import java.sql.SQLException;

@Service
public class ClientServiceImpl implements ClientService{
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createClient(ClientEntity client) throws SQLException {

    }

    @Override
    public ClientEntity getClientById(Long id) throws SQLException {
        return clientRepository.findById(id).orElse(null);
    }
}
