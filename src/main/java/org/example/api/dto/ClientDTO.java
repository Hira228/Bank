package org.example.api.dto;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    Long id;
    @NonNull
    String name;
    @NonNull
    String password;
    @NonNull
    private List<String> emails;
    @NonNull
    private List<String> telephoneNumbers;
    @NonNull
    BigDecimal balance;
    @NonNull
    LocalDate dateOfBirth;
}
