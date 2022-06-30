package com.MindHub.homebanking.controllers;


import com.MindHub.homebanking.dtos.CardPaymentsDTO;
import com.MindHub.homebanking.models.*;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.CardService;
import com.MindHub.homebanking.services.ClientService;
import com.MindHub.homebanking.services.TransactionService;
import com.MindHub.homebanking.utils.CardUtils;
import com.MindHub.homebanking.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static com.MindHub.homebanking.models.TransactionType.CREDITO;
import static com.MindHub.homebanking.models.TransactionType.DEBITO;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class CardController {


    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @PatchMapping(path = "/cards/{id}")
    public ResponseEntity<Object> deleteCard(Authentication authentication,@PathVariable long id){
        Card card = cardService.findById(id);
        Client client = clientService.getClientCurrent(authentication);
        if(card == null){
            return new ResponseEntity<>("no existe el id",HttpStatus.FORBIDDEN);
        }
        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("la tarjeta no pertenece al cliente",HttpStatus.FORBIDDEN);
        }
        card.setState(false);
        cardService.cardSave(card);
        return new ResponseEntity<>("Eliminada",HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/cards/expired/{id}")
    public ResponseEntity<Object> expiredCard(@PathVariable long id){
        Card card = cardService.findById(id);
        if(card == null){
            return new ResponseEntity<>("no existe el id",HttpStatus.FORBIDDEN);
        }
        card.setExpired(true);
        cardService.cardSave(card);
        return new ResponseEntity<>("modificada",HttpStatus.ACCEPTED);
    }


    @CrossOrigin
    @Transactional
    @PostMapping(path = "/clients/cards/payments")
    public ResponseEntity<Object> cardPayments(@RequestBody CardPaymentsDTO cardPaymentsDTO,@RequestParam  String thruDate){


        if(cardPaymentsDTO.getCardNumber().isEmpty()){
            return new ResponseEntity<>("el numero de la tarjeta no puede estar vacio",HttpStatus.FORBIDDEN);
        }

        if(thruDate == null){
            return new ResponseEntity<>("la fecha de la tarjeta no puede estar vacio",HttpStatus.FORBIDDEN);
        }

        if(cardPaymentsDTO.getCvv() <=0){
            return new ResponseEntity<>("el cvv no puede estar vacio",HttpStatus.FORBIDDEN);
        }

        if(cardPaymentsDTO.getAmountPayment() <=0){
            return new ResponseEntity<>("el monto no puede estar vacio",HttpStatus.FORBIDDEN);
        }

        if(cardPaymentsDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("la descripcion no puede estar vacia",HttpStatus.FORBIDDEN);
        }

        Card card = cardService.findByNumber(cardPaymentsDTO.getCardNumber());
        if(card == null){
            return new ResponseEntity<>("no se encontro la tarjeta",HttpStatus.FORBIDDEN);
        }

        String year = "" + card.getThruDate().getYear();
        String mounth = "" + card.getThruDate().getMonth().getValue();
        if(card.getThruDate().getMonth().getValue() < 10){
            mounth = "0" + mounth;
        }
        String date = mounth + "/" + year.substring(2);
        Client client = card.getOwner();
        Account account = client.getAccounts().stream().filter(account1 -> account1.getBalance()>cardPaymentsDTO.getAmountPayment()).findFirst().orElse(null);

        if(!date.equals(thruDate)){
            return new ResponseEntity<>("las fechas no coinciden",HttpStatus.FORBIDDEN);
        }


        if(account == null){
            return new ResponseEntity<>("Saldo Insuficiente",HttpStatus.FORBIDDEN);
        }


        if(card.getCvv() != cardPaymentsDTO.getCvv()){
            return new ResponseEntity<>("el cvv no coincide",HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("la tarjeta no pertenece al cliente",HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("la tarjeta esta vencida",HttpStatus.FORBIDDEN);
        }

        if(!card.getCardholder().equals(cardPaymentsDTO.getName())){
            return new ResponseEntity<>("el nombre no coincide",HttpStatus.FORBIDDEN);
        }


        account.setBalance(account.getBalance() - cardPaymentsDTO.getAmountPayment() );
        accountService.AccountSave(account);
        Transaction transaction1 = new Transaction(account.getBalance(),true,cardPaymentsDTO.getDescription(),cardPaymentsDTO.getAmountPayment()*-1,DEBITO,LocalDate.now(), account);
        transactionService.TransactionSave(transaction1);

        return new ResponseEntity<>("pago exitoso",HttpStatus.CREATED);
    }


    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> cards(

            @RequestParam CardType type, @RequestParam CardColor color, Authentication authentication) {
            Client client = clientService.getClientCurrent(authentication);


        Set<Card> cards = client.getCards();
        cards = cards.stream().filter(card -> card.getType().equals(type)).collect(Collectors.toSet());
        Set<Card> colors;
        colors = cards.stream().filter(card -> card.getColor().equals(color)).collect(Collectors.toSet());

        if(cards.size() >=3){
            return new ResponseEntity<>("ya tienes 3 tarjetas de este tipo", HttpStatus.FORBIDDEN);
        }
        if(colors.size() >= 1){
            return new ResponseEntity<>("ya tienes 1 tarjeta de este color", HttpStatus.FORBIDDEN);
        }


        Card card = new Card(false,true,client.getFirstName()+ " "+ client.getLastName(),type,color,
                cardService.getCardNumber(),
                CardUtils.getRandomNumber(100,999),
                LocalDate.now(),LocalDate.now().plusYears(5),client);
        cardService.cardSave(card);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }


}
