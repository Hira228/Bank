package api.dto;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClientDTO {

    public ClientDTO(@NonNull Long id, @NonNull String username,
                     @NonNull String password,
                     @NonNull String email, @NonNull String telephoneNumber,
                     @NonNull BigDecimal balance, @NonNull LocalDateTime dateOfBirth) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.balance = balance;
        this.dateOfBirth = dateOfBirth;
    }
    @NonNull
    private final Long id;
    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @NonNull
    private final String email;
    @NonNull
    private final String telephoneNumber;
    @NonNull
    private final BigDecimal balance;
    @NonNull
    private final LocalDateTime dateOfBirth;

}
