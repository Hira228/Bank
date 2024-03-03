package org.effectivemobile.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.effectivemobile.config.ClientDetails;
import org.effectivemobile.entity.ClientEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientDetailsServiceImpl implements UserDetailsService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ClientEntity> optionalClient = findByNames(username);

        return optionalClient.map(ClientDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    private Optional<ClientEntity> findByNames(String name) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT c FROM ClientEntity c WHERE :username = c.username", ClientEntity.class)
                    .setParameter("username", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
