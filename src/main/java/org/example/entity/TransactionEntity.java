package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @NonNull
    ClientEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @NonNull
    ClientEntity recipient;

    @NonNull
    BigDecimal amount;

    @NonNull
    LocalDateTime timestamp;

}
