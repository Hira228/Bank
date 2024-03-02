package org.effectivemobile.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden=true)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @NonNull
    BankAccountEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @NonNull
    BankAccountEntity recipient;

    @NonNull
    BigDecimal amount;

    @NonNull
    LocalDateTime timestamp;

    public static TransactionEntity makeDefault(BankAccountEntity sender, BankAccountEntity recipient,
                                                BigDecimal amount, LocalDateTime timestamp) {


        return builder().sender(sender)
                .recipient(recipient)
                .amount(amount)
                .timestamp(timestamp).build();
    }

}
