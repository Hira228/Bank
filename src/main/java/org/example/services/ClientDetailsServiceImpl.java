package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entity.ClientEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements UserDetailsService {

    private final ClientServiceImpl clientService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<ClientEntity> optionalClient = clientService.findByEmails(email);

        if (optionalClient.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        ClientEntity client = optionalClient.get();

        return org.springframework.security.core.userdetails.User
                .withUsername(client.getEmails().get(0))
                .password(passwordEncoder.encode(client.getPassword()))
                .roles("CLIENT")
                .build();
    }
}
