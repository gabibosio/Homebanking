package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.dtos.ClientDTO;
import com.MindHub.homebanking.models.Client;
import com.MindHub.homebanking.repositories.ClientRepository;
import com.MindHub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClientsDto(Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            List<Client> clients = clientRepository.findAll();
            clients.stream().forEach(client -> client.setCards(client.getCards().stream().filter(card -> card.isState() == true).collect(Collectors.toSet())));

            clients.stream().forEach(client -> client.setAccounts(client.getAccounts().stream().filter(account ->  account.isVisibility() == true).collect(Collectors.toSet())));
            return  clients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
        }

        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO>  getClientDto(long id) {
        return clientRepository.findById(id).map(client -> new ClientDTO(client));
    }

    @Override
    public Client getClientCurrent(Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            Client client = clientRepository.findByEmail(authentication.getName());
          client.setCards(client.getCards().stream().filter(card -> card.isState() == true).collect(Collectors.toSet()));

          client.setAccounts(client.getAccounts().stream().filter(account -> account.isVisibility()).collect(Collectors.toSet()));
            return  client;
        }
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
