// src/main/java/com/payflow/service/TransactionService.java
package com.payflow.service;

import com.payflow.entity.Transaction;
import com.payflow.entity.User;
import com.payflow.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TransactionService handles all business logic related to money transfers.
 */
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public Transaction sendMoney(Transaction transaction) {
        // Validate sender exists
        User sender = userService.findByUpiId(transaction.getSenderUpiId())
                .orElseThrow(() -> new RuntimeException("Sender not found with UPI ID: " + transaction.getSenderUpiId()));

        // Validate receiver exists
        User receiver = userService.findByUpiId(transaction.getReceiverUpiId())
                .orElseThrow(() -> new RuntimeException("Receiver not found with UPI ID: " + transaction.getReceiverUpiId()));

        // Validate amount is positive
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transaction amount must be positive");
        }

        // Validate sender has sufficient balance
        if (sender.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance. Available: " + sender.getBalance());
        }

        // Process the transaction - deduct from sender
        BigDecimal senderNewBalance = sender.getBalance().subtract(transaction.getAmount());
        userService.updateUserBalance(sender.getId(), senderNewBalance);

        // Process the transaction - add to receiver
        BigDecimal receiverNewBalance = receiver.getBalance().add(transaction.getAmount());
        userService.updateUserBalance(receiver.getId(), receiverNewBalance);

        // Update transaction status
        transaction.setStatus("COMPLETED");
        transaction.setUpdatedAt(LocalDateTime.now());

        // Save the transaction record
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findBySenderUpiId(String senderUpiId) {
        return transactionRepository.findBySenderUpiId(senderUpiId);
    }

    public List<Transaction> findByReceiverUpiId(String receiverUpiId) {
        return transactionRepository.findByReceiverUpiId(receiverUpiId);
    }

    public List<Transaction> findTransactionsBetweenUsers(String upiId1, String upiId2) {
        return transactionRepository.findTransactionsBetweenUsers(upiId1, upiId2);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }
}