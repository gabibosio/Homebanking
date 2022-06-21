package com.MindHub.homebanking.controllers;


import com.MindHub.homebanking.dtos.ClientLoanDTO;
import com.MindHub.homebanking.dtos.LoanApplicationDTO;
import com.MindHub.homebanking.dtos.LoanDTO;
import com.MindHub.homebanking.models.*;
import com.MindHub.homebanking.repositories.*;
import com.MindHub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.MindHub.homebanking.models.TransactionType.CREDITO;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoansDto();
    }

   @PostMapping("/createLoans")
   public ResponseEntity<Object> createLoan(@RequestBody Loan loan){
        Loan loan1 = new Loan(loan.getInterest(),loan.getName(),loan.getMaxAmount(),loan.getPayments());

        if(loan.getName().isEmpty() || loan.getInterest() <=0 || loan.getMaxAmount() <=0 || loan.getPayments().isEmpty()){
            return new ResponseEntity<>("los datos no pueden estar vacios",HttpStatus.FORBIDDEN);
        }

        loanService.saveLoan(loan1);
       return new ResponseEntity<>("prestamo creado",HttpStatus.ACCEPTED);
   }

    @Transactional
    @PostMapping(value="/loans")
    public ResponseEntity<Object> newLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                         Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        Loan loan = loanService.findById(loanApplicationDTO.getId());
        Account account = accountService.findByNumber(loanApplicationDTO.getAccountDestiny());


        if(client.getLoans().contains(loan)){
            return new ResponseEntity<>("prestamo ya solicitado", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getId() <=0 ||  loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getAccountDestiny().isEmpty()){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount().isInfinite() || loanApplicationDTO.getAmount().isNaN()){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("monto invalido",HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("el prestamo no existe", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("El monto excede el limite permitido", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("cuotas no disponibles", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("la cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("la cuenta no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*loan.getInterest()/100 + loanApplicationDTO.getAmount(),loanApplicationDTO.getPayments(),client,loan);
        clientLoanService.ClientLoanServiceSave(clientLoan);

        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        accountService.AccountSave(account);

        Transaction transaction = new Transaction(account.getBalance(),true,"Credito aprobado:"+" "+loan.getName(),loanApplicationDTO.getAmount(),CREDITO, LocalDate.now(), account);
        transactionService.TransactionSave(transaction);



        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
