package org.effectivemobile.api.factory;

import org.effectivemobile.api.dto.ClientDTO;
import org.effectivemobile.entity.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientDTOFactory {

    public ClientDTO createClientDTO(ClientEntity entity) {
        return ClientDTO.builder().id(entity.getId()).username(entity.getUsername())
                .password(entity.getPassword()).emails(entity.getEmails())
                .telephoneNumbers(entity.getTelephoneNumbers())
                .dateOfBirth(entity.getDateOfBirth()).build();
    }

    public List<ClientDTO> createClientDTOList(List<ClientEntity> entities) {
        return entities
                .stream()
                .map(this::createClientDTO)
                .collect(Collectors.toList());
    }
}
