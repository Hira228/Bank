package org.effectivemobile.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.effectivemobile.api.dto.AuthenticateDTO;
import org.effectivemobile.api.dto.AuthenticationRequest;
import org.effectivemobile.api.dto.BankAccountDTO;
import org.effectivemobile.api.dto.ClientDTO;
import org.effectivemobile.api.factory.BankAccountsFactory;
import org.effectivemobile.config.ClientDetails;
import org.effectivemobile.config.JwtService;
import org.effectivemobile.entity.BankAccountEntity;
import org.effectivemobile.entity.ClientEntity;
import org.effectivemobile.repositories.ClientRepository;;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientServiceImpl implements org.effectivemobile.services.Service<ClientEntity, Long> {
    private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
    ClientRepository clientRepository;
    PasswordEncoder passwordEncoder;
    BankAccountServiceImpl bankAccountService;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    BankAccountsFactory bankAccountsFactory;

    public AuthenticateDTO authenticateClient(AuthenticationRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        ClientEntity client = findByUserName(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(new ClientDetails(client));
        return AuthenticateDTO.builder()
                .token(jwtToken)
                .build();
    }
    public AuthenticateDTO registerClient(ClientDTO clientRequest) {
        if (clientRequest == null) {
            throw new IllegalArgumentException("ClientDTO cannot be null");
        }

        String username = clientRequest.getUsername();
        String password = clientRequest.getPassword();
        List<String> emails = clientRequest.getEmails();
        List<String> telephoneNumbers = clientRequest.getTelephoneNumbers();
        BigDecimal balance = clientRequest.getInitialBalance();
        LocalDate dateOfBirth = clientRequest.getDateOfBirth();

        if (!isValidClientData(emails, telephoneNumbers, balance, username)) {
            throw new IllegalArgumentException("Invalid Data");
        }

        ClientEntity client = createEntity(ClientEntity.makeDefault(
                username, password, balance, emails, telephoneNumbers, dateOfBirth));
        String jwtToken = jwtService.generateToken(new ClientDetails(client));


        bankAccountService.createEntity(BankAccountEntity.builder()
                .client(client)
                .accountBalance(balance)
                .creationDate(LocalDateTime.now())
                .build());

        return AuthenticateDTO.builder()
                .token(jwtToken)
                .build();
    }
    @Override
    public ClientEntity createEntity(ClientEntity entity) {
        entity.setRoles("CLIENT");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        ClientEntity client = clientRepository.save(entity);
        logger.info("Client with id {} created successfully", entity.getId());
        return client;
    }

    @Override
    public Optional<ClientEntity> getEntityById(Long aLong) {
        Optional<ClientEntity> clientEntityOptional = clientRepository.findById(aLong);
        if (clientEntityOptional.isEmpty()) {
            logger.warn("Client with ID {} not found", aLong);
        }
        return clientEntityOptional;
    }

    @Override
    public void updateEntity(ClientEntity entity) {
        clientRepository.save(entity);
        logger.info("Updated client: {}", entity);
    }

    @Override
    public void deleteEntityById(Long aLong) {
        clientRepository.deleteById(aLong);
        logger.info("Deleted client with ID {}", aLong);
    }


    public Optional<ClientEntity> findByUserName(String name) {
        return clientRepository.findByFilterUserName(name).stream().findFirst();
    }

    public Optional<ClientEntity> findByEmails(String email) {
        List<ClientEntity> clients = clientRepository.findAll();
        for (ClientEntity client : clients) {
            List<String> clientEmails = client.getEmails();
            if (clientEmails.contains(email)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }


    public Optional<ClientEntity> findByTelephoneNumbers(String telephoneNumber) {
        List<ClientEntity> clients = clientRepository.findAll();
        for (ClientEntity client : clients) {
            List<String> clientTelephoneNumbers = client.getTelephoneNumbers();
            if (clientTelephoneNumbers.contains(telephoneNumber)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ClientEntity> getAllEntities() {
        List<ClientEntity> allClients = clientRepository.findAll();
        logger.info("Retrieved {} clients", allClients.size());
        return allClients;
    }

    public boolean updateEmail(String updatableEmail, String newEmail) {
        Long clientId = getId();
        Optional<ClientEntity> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            ClientEntity client = clientOptional.get();
            if(!client.getEmails().contains(updatableEmail)) {
                logger.warn("There's no such e-mail address");
                return false;
            }
            if (clientRepository.existsByEmailsAndIdNot(newEmail, clientId)) {
                logger.warn("Email {} is already in use by another client", newEmail);
                return false;
            }
            int k = -1;
            for(int i = 0; i < client.getEmails().size(); ++i) {
                if(client.getEmails().get(i) != null && client.getEmails().get(i).equals(updatableEmail)) {
                    k = i;
                    break;
                }
            }
            if(k == -1){
                logger.info("Invalid replacement email client with ID {}", clientId);
                return false;
            }
            client.getEmails().remove(k);
            client.getEmails().add(k, newEmail);
            clientRepository.save(client);
            logger.info("Updated email for client with ID {}: {}", clientId, newEmail);
            return true;
        } else {
            logger.warn("Client with ID {} not found", clientId);
            return false;
        }
    }

    public boolean updateTelephoneNumber(String updatableTelephoneNumber, String newTelephoneNumber) {
        Long clientId = getId();
        Optional<ClientEntity> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            ClientEntity client = clientOptional.get();
            if(!client.getTelephoneNumbers().contains(updatableTelephoneNumber)) {
                logger.warn("There's no such phone number");
                return false;
            }
            if (clientRepository.existsByTelephoneNumbersAndIdNot(newTelephoneNumber, clientId)) {
                logger.warn("Telephone number {} is already in use by another client", newTelephoneNumber);
                return false;
            }
            int k = -1;
            for (int i = 0; i < client.getTelephoneNumbers().size(); ++i) {
                if (client.getTelephoneNumbers().get(i) != null && client.getTelephoneNumbers().get(i).equals(updatableTelephoneNumber)) {
                    k = i;
                    break;
                }
            }
            if (k == -1) {
                logger.info("Invalid replacement telephone number for client with ID {}", clientId);
                return false;
            }
            client.getTelephoneNumbers().remove(k);
            client.getTelephoneNumbers().add(k, newTelephoneNumber);
            clientRepository.save(client);
            logger.info("Updated telephone number for client with ID {}: {}", clientId, newTelephoneNumber);
            return true;
        } else {
            logger.warn("Client with ID {} not found", clientId);
            return false;
        }
    }

    public List<BankAccountDTO> searchClients(LocalDate dateOfBirth, String telephoneNumber, String name, String email) {
        List<ClientEntity> clients;
        if (dateOfBirth != null) {
            clients = clientRepository.findByFilterDate(dateOfBirth);
        } else if (telephoneNumber != null) {
            clients = clientRepository.findByFilterTelephoneNumber(telephoneNumber);
        } else if (name != null) {
            clients = clientRepository.findByFilterUserName(name);
        } else if (email != null) {
            clients = clientRepository.findByFilterEmail(email);
        } else {
            clients = clientRepository.findAll();
        }

        return clients.stream()
                .map(client -> {
                    BankAccountEntity bankAccount = bankAccountService.getEntityById(client.getId())
                            .orElse(null);
                    return bankAccount != null ? bankAccountsFactory.createBankAccountDTO(bankAccount) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public boolean removeEmail(String email) {
        Long client = getId();
        Optional<ClientEntity> client1 = getEntityById(client);
        if(client1.isEmpty()) return false;
        if(client1.get().getEmails().size() == 1 ||
                !client1.get().getEmails().contains(email)) return false;
        int k = -1;
        for(int i = 0; i < client1.get().getEmails().size(); ++i) {
            if(client1.get().getEmails().get(i).equals(email)) {
                k = i;
                break;
            }
        }
        if(k == -1) return false;
        client1.get().getEmails().remove(k);
        clientRepository.save(client1.get());
        return true;
    }

    public boolean removeTelephoneNumber(String telephoneNumber) {
        Long client = getId();
        Optional<ClientEntity> client1 = getEntityById(client);
        if(client1.isEmpty()) return false;
        if(client1.get().getTelephoneNumbers().size() == 1 ||
                !client1.get().getTelephoneNumbers().contains(telephoneNumber)) return false;
        int k = -1;
        for(int i = 0; i < client1.get().getTelephoneNumbers().size(); ++i) {
            if(client1.get().getTelephoneNumbers().get(i).equals(telephoneNumber)) {
                k = i;
                break;
            }
        }
        if(k == -1) return false;
        client1.get().getTelephoneNumbers().remove(k);
        clientRepository.save(client1.get());
        return true;
    }

    private boolean isValidClientData(List<String> emails, List<String> telephoneNumbers, BigDecimal balance, String username) {
        for (String email : emails) {
            if (findByEmails(email).isPresent()) {
                return false;
            }
        }
        for (String phoneNumber : telephoneNumbers) {
            if (findByTelephoneNumbers(phoneNumber).isPresent()) {
                return false;
            }
        }
        if(findByUserName(username).isPresent()) return false;


        return balance.compareTo(BigDecimal.ZERO) >= 0;
    }

    private Long getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getId();
    }

    public boolean addNewTelephoneNumber(String newTelephoneNumber) {
        Optional<ClientEntity> client = clientRepository.findById(getId());
        if(findByTelephoneNumbers(newTelephoneNumber).isPresent()) return false;
        client.get().getTelephoneNumbers().add(newTelephoneNumber);
        clientRepository.save(client.get());
        return true;
    }

    public boolean addNewEmailAddress(String newEmailAddress) {
        Optional<ClientEntity> client = clientRepository.findById(getId());
        if(findByEmails(newEmailAddress).isPresent()) return false;
        client.get().getEmails().add(newEmailAddress);
        clientRepository.save(client.get());
        return true;
    }
}