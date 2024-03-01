package org.example.entity;

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
    String name;
    @NonNull
    String password;
    @NonNull
    BigDecimal balance;
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

    String roles;

    public boolean removeEmail(String email) {
        int k = -1;
        for(int i = 0; i < emails.size(); ++i) {
            if(emails.get(i).equals(email)) {
                k = i;
                break;
            }
        }
        if(k == -1) return false;
        emails.remove(k);
        return true;
    }

    public boolean removeTelephoneNumber(String telephoneNumber) {
        int k = -1;
        for(int i = 0; i < telephoneNumbers.size(); ++i) {
            if(telephoneNumbers.get(i).equals(telephoneNumber)) {
                k = i;
                break;
            }
        }
        if(k == -1) return false;
        telephoneNumbers.remove(k);
        return true;
    }

    public boolean checkTelephoneNumbers(String telephoneNumber) {
        for(String t : telephoneNumbers) {
            if(t.equals(telephoneNumber)){
                return true;
            }
        }
        return false;
    }

    public boolean checkEmail(String email) {
        for(String t : emails) {
            if(t.equals(email)){
                return true;
            }
        }
        return false;
    }

    public static ClientEntity makeDefault(String name, String password, BigDecimal balance,
                                           List<String> emails, List<String> telephoneNumbers,
                                           LocalDate dateOfBirth) {


        return builder().name(name)
                .password(password)
                .balance(balance)
                .emails(emails)
                .telephoneNumbers(telephoneNumbers)
                .dateOfBirth(dateOfBirth).build();
    }
}
