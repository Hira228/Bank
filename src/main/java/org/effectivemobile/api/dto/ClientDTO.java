package org.effectivemobile.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
    @Schema(hidden = true)
    Long id;
    @NotNull
    String username;
    @NotNull
    String password;
    @NotNull
    private List<String> emails;
    @NotNull
    private List<String> telephoneNumbers;
    @NotNull
    BigDecimal initialBalance;
    @NotNull
    LocalDate dateOfBirth;

    @Schema(hidden = true)
    String roles;
}
