package com.MindHub.homebanking;

import com.MindHub.homebanking.models.*;
import com.MindHub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.time.LocalDate;
import java.util.List;

import static com.MindHub.homebanking.models.CardColor.*;
import static com.MindHub.homebanking.models.TransactionType.CREDITO;
import static com.MindHub.homebanking.models.TransactionType.DEBITO;


@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEnconder;

	@Bean
	public CommandLineRunner initData(ClientRepository repositoryclient, AccountRepository repositoryaccount, TransactionRepositoy repositoyTransaction, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository) {
		return (args) -> {
			Client client1 = new Client("Melba","Morel","melba@mindhub.com", passwordEnconder.encode("melba"));
			repositoryclient.save(client1);
			Client client2 = new Client("admin","admin","admin@admin.com",passwordEnconder.encode("asd123"));
			repositoryclient.save(client2);
			Client client3 = new Client("Juan","Perez","juanperez@gmail.com",passwordEnconder.encode("1234"));
			repositoryclient.save(client3);



			Account account1 = new Account(AccountType.Corriente,true,client1,"VIN001", LocalDate.now(),5000);
            repositoryaccount.save(account1);
			Account account2 = new Account(AccountType.Ahorro,true,client1,"VIN002", LocalDate.now().plusDays(1),7500);
			repositoryaccount.save(account2);

			Account account3 = new Account(AccountType.Ahorro,true,client3,"VIN003", LocalDate.now().plusDays(1),1000000);
			repositoryaccount.save(account3);



			Transaction transaction1 = new Transaction(account1.getBalance(),true,"Alquiler",5000,CREDITO,LocalDate.now(), account1);
			repositoyTransaction.save(transaction1);

			Transaction transaction2 = new Transaction(account1.getBalance(),true,"ingresos",-3000,DEBITO,LocalDate.now().plusDays(5), account1);
			repositoyTransaction.save(transaction2);

			Transaction transaction3 = new Transaction(account2.getBalance(),true,"ingresos",8000,CREDITO,LocalDate.now(), account2);
			repositoyTransaction.save(transaction3);
			Transaction transaction4 = new Transaction(account2.getBalance(),true,"Netflix",-2000,DEBITO,LocalDate.now(), account2);
			repositoyTransaction.save(transaction4);


            Loan Hipotecario = new Loan(20,"Hipotecario",500000, List.of(12,24,36,48,60));
			loanRepository.save(Hipotecario);

			Loan Personal = new Loan(10,"Personal",100000,List.of(6,12,24));
			loanRepository.save(Personal);

			Loan Automotriz = new Loan(15,"Automotriz",300000,List.of(6,12,24,36));
			loanRepository.save(Automotriz);

			ClientLoan clientLoan1 = new ClientLoan(400000.0,60,client1,Hipotecario);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000.0,12,client1,Personal);
			clientLoanRepository.save(clientLoan2);

            Card card1 = new Card(false,true,client1.getFirstName()+ " "+ client1.getLastName(),CardType.DEBITO,GOLD,"6556-4265-1565-8545",654,
					LocalDate.now(),LocalDate.now().plusYears(5),client1);
			cardRepository.save(card1);
			Card card2 = new Card(false,true,client1.getFirstName()+ " "+ client1.getLastName(),CardType.CREDITO,TITANIUM,"2624-1151-1995-5548",356,
					LocalDate.now(),LocalDate.now().plusYears(5),client1);
			cardRepository.save(card2);
			Card card3 = new Card(false,true,client1.getFirstName()+ " "+ client1.getLastName(),CardType.CREDITO,SILVER,"1518-5629-4454-5483",498,
					LocalDate.now(),LocalDate.now().plusYears(-5),client1);
			cardRepository.save(card3);

			Card card4 = new Card(false,true,client3.getFirstName()+ " "+ client3.getLastName(),CardType.CREDITO,SILVER,"8864-2940-3229-6139",388,
					LocalDate.now(),LocalDate.now().plusYears(5),client3);
			cardRepository.save(card4);

		};
	}



}
