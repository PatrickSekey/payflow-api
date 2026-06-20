// src/main/java/com/payflow/controller/TransactionController.java
package com.payflow.controller;

import com.payflow.entity.Transaction;
import com.payflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * TransactionController handles all HTTP requests related to Transaction operations.
 * Base path: /transactions
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /transactions - Send money between users
     *
     * @RequestBody binds the HTTP request body to the Transaction object.
     *
     * Example curl:
     * curl -X POST http://localhost:8080/transactions \
     *   -H "Content-Type: application/json" \
     *   -d '{"senderUpiId":"john@payflow","receiverUpiId":"jane@payflow","amount":100.00,"description":"Payment for lunch"}'
     */
    @PostMapping
    public ResponseEntity<Transaction> sendMoney(@RequestBody Transaction transaction) {
        try {
            Transaction processedTransaction = transactionService.sendMoney(transaction);
            return new ResponseEntity<>(processedTransaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /transactions - Get all transactions
     *
     * Example curl:
     * curl -X GET http://localhost:8080/transactions
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * GET /transactions/{id} - Get transaction by ID
     *
     * Example curl:
     * curl -X GET http://localhost:8080/transactions/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /transactions/sender/{senderUpiId} - Get transactions by sender UPI ID
     *
     * Example curl:
     * curl -X GET http://localhost:8080/transactions/sender/john@payflow
     */
    @GetMapping("/sender/{senderUpiId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySender(@PathVariable String senderUpiId) {
        List<Transaction> transactions = transactionService.findBySenderUpiId(senderUpiId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * GET /transactions/receiver/{receiverUpiId} - Get transactions by receiver UPI ID
     *
     * Example curl:
     * curl -X GET http://localhost:8080/transactions/receiver/jane@payflow
     */
    @GetMapping("/receiver/{receiverUpiId}")
    public ResponseEntity<List<Transaction>> getTransactionsByReceiver(@PathVariable String receiverUpiId) {
        List<Transaction> transactions = transactionService.findByReceiverUpiId(receiverUpiId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * GET /transactions/between/{upiId1}/{upiId2} - Get transactions between two users
     *
     * Example curl:
     * curl -X GET http://localhost:8080/transactions/between/john@payflow/jane@payflow
     */
    @GetMapping("/between/{upiId1}/{upiId2}")
    public ResponseEntity<List<Transaction>> getTransactionsBetweenUsers(
            @PathVariable String upiId1,
            @PathVariable String upiId2) {
        List<Transaction> transactions = transactionService.findTransactionsBetweenUsers(upiId1, upiId2);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}