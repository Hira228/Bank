package org.example.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.entity.BankAccountEntity;
import org.example.entity.ClientEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDTO {
    @NonNull
    UUID id;

    @NonNull
    BankAccountEntity sender;

    @NonNull
    BankAccountEntity recipient;

    @NonNull
    BigDecimal amount;

    @NonNull
    LocalDateTime timestamp;
}
