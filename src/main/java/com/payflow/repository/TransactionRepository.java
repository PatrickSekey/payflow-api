// src/main/java/com/payflow/repository/TransactionRepository.java
package com.payflow.repository;

import com.payflow.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * TransactionRepository provides CRUD operations for Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Derived query method to find transactions by sender UPI ID.
     * JPA generates: SELECT * FROM transaction WHERE sender_upi_id = ?
     */
    List<Transaction> findBySenderUpiId(String senderUpiId);

    /**
     * Derived query method to find transactions by receiver UPI ID.
     * JPA generates: SELECT * FROM transaction WHERE receiver_upi_id = ?
     */
    List<Transaction> findByReceiverUpiId(String receiverUpiId);

    /**
     * Custom JPQL query to find all transactions between two users.
     * This demonstrates using JPQL with multiple parameters.
     */
    @Query("SELECT t FROM Transaction t WHERE (t.senderUpiId = :upiId1 AND t.receiverUpiId = :upiId2) OR (t.senderUpiId = :upiId2 AND t.receiverUpiId = :upiId1)")
    List<Transaction> findTransactionsBetweenUsers(@Param("upiId1") String upiId1, @Param("upiId2") String upiId2);
}