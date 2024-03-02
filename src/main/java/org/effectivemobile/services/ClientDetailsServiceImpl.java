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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<ClientEntity> optionalClient = findByEmails(email);

        return optionalClient.map(ClientDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }

    private Optional<ClientEntity> findByEmails(String email) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT c FROM ClientEntity c JOIN FETCH c.emails WHERE :email MEMBER OF c.emails", ClientEntity.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}