package api.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;



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
     String username;
    @NonNull
    String password;
    @NonNull
    String email;
    @NonNull
    String telephoneNumber;
    @NonNull
    BigDecimal balance;
    @NonNull
    LocalDateTime dateOfBirth;
}
