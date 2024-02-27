package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "client")
public class ClientEntity {

    public ClientEntity(Long id, String username,
                        String password, String email,
                        String telephoneNumber,
                        BigDecimal balance,
                        LocalDateTime dateOfBirth) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.balance = balance;
        this.dateOfBirth = dateOfBirth;
    }
    public ClientEntity(String username, String password, String email,
                        String telephoneNumber, BigDecimal balance,
                        LocalDateTime dateOfBirth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.balance = balance;
        this.dateOfBirth = dateOfBirth;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String telephoneNumber;
    @Column(nullable = false)
    private BigDecimal balance;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDateTime dateOfBirth;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
