package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
public class BankAccountEntity {
    public BankAccountEntity(Long id, ClientEntity client, BigDecimal balance, LocalDateTime creationDate) {
        this.id = id;
        this.client = client;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private final ClientEntity client;

    @Column(name = "balance")
    private final BigDecimal balance;

    @Column(name = "creation_date")
    private final LocalDateTime creationDate;

    public Long getId() {
        return id;
    }

    public ClientEntity getClient() {
        return client;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
