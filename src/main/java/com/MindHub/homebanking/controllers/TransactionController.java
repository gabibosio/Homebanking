package com.MindHub.homebanking.controllers;


import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.models.Client;
import com.MindHub.homebanking.models.Transaction;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.ClientService;
import com.MindHub.homebanking.services.TransactionService;
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

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
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

    @PostMapping(path = "/pdf/{id}")
    public ResponseEntity<Object> createPdf(Authentication authentication,@PathVariable long id,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta)
            throws FileNotFoundException, DocumentException {
        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.findByID(id);
        if(account == null){
            return new ResponseEntity<>("la cuenta no existe",HttpStatus.FORBIDDEN);
        }

        Set <Transaction> transaction = account.getTransactions();
        Set<Transaction> transactionSet = transaction.stream().filter(transaction1 -> transaction1.getDate().isBefore(hasta.plusDays(1))).collect(Collectors.toSet());
        transactionSet.stream().filter(transaction1 -> transaction1.getDate().isAfter(desde));

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("la cuenta no pertenece al cliente",HttpStatus.FORBIDDEN);
        }


        Document document = new Document();
        String ruta = System.getProperty("user.home");
        PdfWriter.getInstance(document,new FileOutputStream(ruta + "/Desktop/Transacciones.pdf"));


        document.open();

        Phrase p = new Phrase("Cuenta: "+account.getNumber());
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Fecha");
        table.addCell("Descripcion");
        table.addCell("Tipo");
        table.addCell("Monto");
        table.addCell("Saldo Actual");

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(new Color(98, 56, 232));
        }

        transactionSet.stream().forEach(transaction1 -> {
            PdfPCell c1 = new PdfPCell(new Phrase(transaction1.getDate() + ""));
            PdfPCell c2 = new PdfPCell(new Phrase(transaction1.getDescription()));
            PdfPCell c3 = new PdfPCell(new Phrase(transaction1.getType() + ""));
            PdfPCell c4 = new PdfPCell(new Phrase(transaction1.getAmount() + ""));
            PdfPCell c5 = new PdfPCell(new Phrase(transaction1.getBalance() + ""));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        });
        document.add(table);

        document.close();

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
