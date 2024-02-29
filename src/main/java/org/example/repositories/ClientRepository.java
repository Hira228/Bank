package org.example.repositories;

import org.example.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
//
//{
//        "name": "Mama Kaka Lala",
//        "password": "123124124",
//        "emails": ["kaka.doe@example.com", "lala@gmail.com"],
//        "telephoneNumbers": ["1231232", "123524232"],
//        "balance": 1000.00,
//        "dateOfBirth": "1990-01-01T00:00:00"
//        }


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByEmailsAndIdNot(String email, Long id);
    boolean existsByTelephoneNumbersAndIdNot(String telephoneNumber, Long id);

    @Query("SELECT c FROM ClientEntity c " +
            "WHERE (:dateOfBirth IS NULL OR c.dateOfBirth > :dateOfBirth) " +
            "AND (:telephoneNumber IS NULL OR :telephoneNumber MEMBER OF c.telephoneNumbers) " +
            "AND (:name IS NULL OR c.name LIKE :name) " +
            "AND (:email IS NULL OR :email MEMBER OF c.emails)")
    List<ClientEntity> findClientsByCriteria(
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("telephoneNumber") String telephoneNumber,
            @Param("name") String name,
            @Param("email") String email
    );
}