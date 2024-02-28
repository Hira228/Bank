package api.controller;


import api.dto.AckDTO;
import api.dto.ClientDTO;
import api.factory.ClientDTOFactory;
import entity.ClientEntity;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repositories.ClientRepository;
import services.ClientServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RestController

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientController {
    ClientServiceImpl clientService;
    ClientDTOFactory clientDTOFactory;
    public static final String FETCH_CLIENT = "/api/client/";
    public static final String CREATE_CLIENT = "/api/client/add";
    public static final String DELETE_CLIENT = "/api/client/{id}";


    @GetMapping(FETCH_CLIENT)
    public ResponseEntity<List<ClientDTO>> fetchClients() {
        List<ClientEntity> clients = clientService.getAllEntities();

        return ResponseEntity.ok(clientDTOFactory.createClientDTOList(clients));
    }

    @PostMapping(CREATE_CLIENT)
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientRequest) {
        String username = clientRequest.getUsername();
        String password = clientRequest.getPassword();
        String email = clientRequest.getEmail();
        String telephoneNumber = clientRequest.getTelephoneNumber();
        BigDecimal balance = clientRequest.getBalance();
        LocalDateTime dateOfBirth = clientRequest.getDateOfBirth();
//        if (!isValidClientData(username, password, email, telephoneNumber)) {
//            return ResponseEntity.badRequest().build();
//        }

        ClientEntity client = clientService.createEntity((ClientEntity.makeDefault(username,
                password, email, telephoneNumber, balance, dateOfBirth)));

        return ResponseEntity.status(HttpStatus.CREATED).body(clientDTOFactory.createClientDTO(client));
    }

    @DeleteMapping(DELETE_CLIENT)
    public ResponseEntity<AckDTO> deleteClient(@PathVariable Long id) {
        if(clientService.getEntityById(id).isPresent()) {
            clientService.deleteEntityById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AckDTO(true));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AckDTO(false));
    }
    private boolean isValidClientData(String username, String password, String email, String telephoneNumber) {
        return clientService.getEntityByEmail(email).isEmpty() &&
                clientService.getEntityByUsername(username).isEmpty() &&
                clientService.getEntityByTelephoneNumber(telephoneNumber).isEmpty(); //&&
               //isValidPassword(password);
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
