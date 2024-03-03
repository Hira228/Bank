package org.effectivemobile.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    @NonNull
    String username;
    @NonNull
    String password;
    @NonNull
    BigDecimal initialBalance;
    @ElementCollection
    @CollectionTable(name = "client_emails", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "email")
    private List<String> emails;

    @ElementCollection
    @CollectionTable(name = "client_telephone_numbers", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "telephone_number")
    private List<String> telephoneNumbers;
    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;

    @Schema(hidden=true)
    String roles;

    public static ClientEntity makeDefault(String username, String password, BigDecimal initialBalance,
                                           List<String> emails, List<String> telephoneNumbers,
                                           LocalDate dateOfBirth) {


        return builder().username(username)
                .password(password)
                .initialBalance(initialBalance)
                .emails(emails)
                .telephoneNumbers(telephoneNumbers)
                .dateOfBirth(dateOfBirth).build();
    }
}
