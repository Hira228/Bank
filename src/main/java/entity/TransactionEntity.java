package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ClientEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private ClientEntity recipient;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date_time")
    private LocalDateTime timestamp;

    public UUID getId() {
        return id;
    }

    public ClientEntity getSender() {
        return sender;
    }

    public void setSender(ClientEntity sender) {
        this.sender = sender;
    }

    public ClientEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(ClientEntity recipient) {
        this.recipient = recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
