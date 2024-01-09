package com.example.xpresspaymentapi.model.repository;

import com.example.xpresspaymentapi.model.entity.VtuAirtimeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VtuAirtimeTransactionRepository extends JpaRepository<VtuAirtimeTransaction, Long> {
}
