package com.MindHub.homebanking.services;

import com.MindHub.homebanking.dtos.ClientDTO;
import com.MindHub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDTO> getClientsDto(Authentication authentication);
    Optional<ClientDTO> getClientDto(long id);
    Client getClientCurrent(Authentication authentication);
    void saveClient(Client client);
    Client findByEmail(String email);
    Client findById(long id);
}
