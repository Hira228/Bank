package org.example.repositories;

import org.example.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByEmailsAndIdNot(String email, Long id);
    boolean existsByTelephoneNumbersAndIdNot(String telephoneNumber, Long id);

    @Query("SELECT c FROM ClientEntity c " +
            "WHERE c.dateOfBirth > :dateOfBirth")
    List<ClientEntity> findByFilterDate(@Param("dateOfBirth") LocalDate dateOfBirth);

    @Query("SELECT c FROM ClientEntity c " +
            "WHERE :telephoneNumber MEMBER OF c.telephoneNumbers")
    List<ClientEntity> findByFilterTelephoneNumber(@Param("telephoneNumber") String telephoneNumber);


    @Query("SELECT c FROM ClientEntity c " +
            "WHERE :email MEMBER OF c.emails")
    List<ClientEntity> findByFilterEmail(@Param("email") String email);

    @Query("SELECT c FROM ClientEntity c " +
            "WHERE c.name LIKE CONCAT(:name, '%')")
    List<ClientEntity> findByFilterName(@Param("name") String name);
}