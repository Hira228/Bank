package org.example.repositories;

import org.example.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> { }
