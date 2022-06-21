package com.MindHub.homebanking.controllers;

import com.MindHub.homebanking.dtos.ClientDTO;
import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.models.AccountType;
import com.MindHub.homebanking.models.Client;
import com.MindHub.homebanking.repositories.AccountRepository;

import com.MindHub.homebanking.repositories.ClientRepository;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.ClientService;
import com.MindHub.homebanking.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(Authentication authentication) {
        return clientService.getClientsDto(authentication);
    }

    @GetMapping("clients/{id}")
    public Optional<ClientDTO> getClient(@PathVariable Long id) {
        //return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
        return clientService.getClientDto(id);
    }


    @PostMapping(path="clients/create")
    public ResponseEntity<Object> createClient(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password){
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("los datos no pueden estar vacios",HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientService.saveClient(client);
        return new ResponseEntity<>("creado",HttpStatus.ACCEPTED);
    }

    @PatchMapping(path="/clients/modificar/{id}")
    public ResponseEntity<Object>  modificarClient (@PathVariable long id,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email){
        Client client = clientService.findById(id);

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            return new ResponseEntity<>("los datos no pueden estar vacios",HttpStatus.FORBIDDEN);
        }

        if(client == null){
            return new ResponseEntity<>("no se encontro el cliente",HttpStatus.FORBIDDEN);
        }

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        clientService.saveClient(client);
        return new ResponseEntity<>("modificado",HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }


        if (clientService.findByEmail(email)!= null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }



            Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
            clientService.saveClient(client);


            Account account = new Account(AccountType.Corriente,true,client, "VIN"+Util.getRandomNumber(100000,99999999), LocalDate.now(), 0);
            accountService.AccountSave(account);
            return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/clients/current")
    public ClientDTO getUser(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return new ClientDTO(client);
    }
}
