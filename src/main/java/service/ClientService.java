package service;

import entity.ClientEntity;

import java.sql.SQLException;

public interface ClientService {
    void createClient(ClientEntity client) throws SQLException;
    ClientEntity getClientById(Long id) throws SQLException;
}
