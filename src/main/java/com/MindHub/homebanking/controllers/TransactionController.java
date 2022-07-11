package com.MindHub.homebanking.controllers;


import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.models.Client;
import com.MindHub.homebanking.models.Transaction;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.ClientService;
import com.MindHub.homebanking.services.TransactionService;
import com.MindHub.homebanking.services.implement.PDFGeneratorServiceImpl;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.MindHub.homebanking.models.TransactionType.CREDITO;
import static com.MindHub.homebanking.models.TransactionType.DEBITO;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    private final PDFGeneratorServiceImpl pdfGeneratorService;

    public  TransactionController(PDFGeneratorServiceImpl pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/pdf/generate/{id}")
    public ResponseEntity<Object> generatePDF(HttpServletResponse response, @PathVariable long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta, Authentication authentication) throws IOException, DocumentException {

        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.findByID(id);
        if(account == null){
            return new ResponseEntity<>("la cuenta no existe",HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("la cuenta no pertenece al cliente",HttpStatus.FORBIDDEN);
        }

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        this.pdfGeneratorService.export(response,id,desde,hasta);
        return new ResponseEntity<>("creado",HttpStatus.ACCEPTED);
    }
    

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> transaction(
            Authentication authentication,
            @RequestParam String description, @RequestParam String accountOrigin,

            @RequestParam String accountDestiny, @RequestParam Double amount) {
        Client client = clientService.getClientCurrent(authentication);
        Account originAccount = accountService.findByNumber(accountOrigin);
        Account destinyAccount = accountService.findByNumber(accountDestiny);


        if (description.isEmpty() || accountOrigin.isEmpty() || accountDestiny.isEmpty() || amount.isNaN()|| amount.isInfinite()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(amount <= 0){
            return new ResponseEntity<>("monto invalido",HttpStatus.FORBIDDEN);
        }

        if(accountDestiny.equals(accountOrigin)){
            return new ResponseEntity<>("No puede transferir a la misma cuenta", HttpStatus.FORBIDDEN);
        }

        if(originAccount == null){
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("La cuenta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        if(destinyAccount == null){
            return new ResponseEntity<>("La Cuenta de Destino no existe", HttpStatus.FORBIDDEN);
        }
        if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("No tiene dinero suficiente para realizar esta transferencia", HttpStatus.FORBIDDEN);
        }

       originAccount.setBalance(originAccount.getBalance() - amount);
        accountService.AccountSave(originAccount);
        destinyAccount.setBalance(destinyAccount.getBalance() + amount);
        accountService.AccountSave(destinyAccount);

        Transaction transaction1 = new Transaction(originAccount.getBalance(),true,description + "-"+destinyAccount.getNumber(),amount * -1,DEBITO,LocalDate.now(), originAccount);
        transactionService.TransactionSave(transaction1);

        Transaction transaction2 = new Transaction(destinyAccount.getBalance(),true,description + "-" + originAccount.getNumber(),amount,CREDITO,LocalDate.now(), destinyAccount);
        transactionService.TransactionSave(transaction2);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }
}
