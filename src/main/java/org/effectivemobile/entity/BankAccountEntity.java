package org.effectivemobile.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden=true)
    Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NonNull
    ClientEntity client;

    @NonNull
    BigDecimal accountBalance;

    @NonNull
    LocalDateTime creationDate;

}
