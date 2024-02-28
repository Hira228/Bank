package repositories;

import api.dto.ClientDTO;
import entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmail(String email);
    Optional<ClientEntity> findByUsername(String username);

    Optional<ClientEntity> findByTelephoneNumber(String telephoneNumber);
}
