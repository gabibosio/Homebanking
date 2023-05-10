package com.MindHub.homebanking.repositories;

import com.MindHub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransactionRepositoy extends JpaRepository <Transaction, Long> {
}
