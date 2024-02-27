package api.factory;

import api.dto.ClientDTO;
import entity.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientDTOFactory {

    public ClientDTO createClientDTO(ClientEntity entity) {
        return new ClientDTO(entity.getId(), entity.getUsername(),
                entity.getPassword(), entity.getEmail(),
                entity.getTelephoneNumber(), entity.getBalance(), entity.getDateOfBirth());
    }

    public List<ClientDTO> createClientDTOList(List<ClientEntity> entities) {
        return entities
                .stream()
                .map(this::createClientDTO)
                .collect(Collectors.toList());
    }
}
