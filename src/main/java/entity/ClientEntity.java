package entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false, unique = true)
    String telephoneNumber;
    @Column(nullable = false)
    BigDecimal balance;
    @Column(name = "date_of_birth", nullable = false)
    LocalDateTime dateOfBirth;

    public static ClientEntity makeDefault(String username, String password,
                                           String email, String telephoneNumber,
                                           BigDecimal balance, LocalDateTime dateOfBirth) {
        return builder().username(username)
                .password(password)
                .email(email)
                .telephoneNumber(telephoneNumber)
                .balance(balance)
                .dateOfBirth(dateOfBirth).build();
    }
}
