package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    public TransactionEntity(UUID id, ClientEntity sender, ClientEntity recipient, BigDecimal amount, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private final ClientEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private final ClientEntity recipient;

    @Column(name = "amount")
    private final BigDecimal amount;

    @Column(name = "date_time")
    private final LocalDateTime timestamp;

    public UUID getId() {
        return id;
    }

    public ClientEntity getSender() {
        return sender;
    }

    public ClientEntity getRecipient() {
        return recipient;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
