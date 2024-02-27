package api.controller;


import api.dto.AckDTO;
import api.dto.ClientDTO;
import api.exceptions.ClientNotFoundException;
import api.factory.ClientDTOFactory;
import entity.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repositories.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ClientController {
    private final ClientRepository clientRepository;
    private final ClientDTOFactory clientDTOFactory;

    @Autowired
    public ClientController(ClientRepository clientRepository, ClientDTOFactory clientDTOFactory) {
        this.clientRepository = clientRepository;
        this.clientDTOFactory = clientDTOFactory;
    }

    public static final String FETCH_CLIENT = "api/client";
    public static final String CREATE_CLIENT = "/api/client/{username}";
    public static final String DELETE_CLIENT = "/api/client/{id}";


    @GetMapping(FETCH_CLIENT)
    public ResponseEntity<List<ClientDTO>> fetchClients(@RequestParam String filter) {
        List<ClientEntity> clients = clientRepository.findAll();

        return ResponseEntity.ok(clientDTOFactory.createClientDTOList(clients));
    }

    @PostMapping(CREATE_CLIENT)
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientEntity clientRequest) {
        String username = clientRequest.getUsername();
        String password = clientRequest.getPassword();
        String email = clientRequest.getEmail();
        String telephoneNumber = clientRequest.getTelephoneNumber();
        BigDecimal balance = clientRequest.getBalance();
        LocalDateTime dateOfBirth = clientRequest.getDateOfBirth();

        if (!isValidClientData(username, password, email, telephoneNumber)) {
            return ResponseEntity.badRequest().build();
        }

        ClientEntity newClient = new ClientEntity(username, password, email,
                telephoneNumber, balance, dateOfBirth);

        clientRepository.save(newClient);

        return ResponseEntity.ok(clientDTOFactory.createClientDTO(newClient));
    }

    @DeleteMapping(DELETE_CLIENT)
    public ResponseEntity<AckDTO> deleteClient(Long clientId) {
        if(clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
        }
        return ResponseEntity.ok(new AckDTO(true));
    }

    private boolean isValidClientData(String username, String password, String email, String telephoneNumber) {
        return clientRepository.findByEmail(email).isEmpty() &&
                clientRepository.findByUsername(username).isEmpty() &&
                clientRepository.findByTelephoneNumber(telephoneNumber).isEmpty() &&
                isValidPassword(password);
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return false;
        }

        return true;
    }
}
