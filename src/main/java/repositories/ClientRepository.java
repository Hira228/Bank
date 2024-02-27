package repositories;

import entity.ClientEntity;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository implements CrudRepository<ClientEntity, Long>{
    @Override
    public Optional<ClientEntity> findById(Long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<ClientEntity> findAll() throws SQLException {
        return null;
    }

    @Override
    public void save(ClientEntity entity) throws SQLException {

    }

    @Override
    public void update(ClientEntity entity) throws SQLException {

    }

    @Override
    public void delete(Long id) throws SQLException {

    }
}
