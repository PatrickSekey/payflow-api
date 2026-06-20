// src/main/java/com/payflow/controller/UserController.java
package com.payflow.controller;

import com.payflow.entity.User;
import com.payflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * UserController handles all HTTP requests related to User operations.
 * Base path: /users
 *
 * @RestController combines @Controller and @ResponseBody
 * It tells Spring that this class handles HTTP requests and the return values
 * should be bound to the web response body (JSON by default)
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /users - Register a new user
     *
     * @RequestBody binds the HTTP request body to the User object.
     * Spring uses Jackson (or other HTTP message converters) to deserialize
     * the JSON request body into a Java User object.
     *
     * Without @RequestBody, Spring would not know to read from the request body
     * and would instead try to use other parameter binding mechanisms
     * (like query parameters or form data), resulting in all fields being null.
     *
     * Example curl:
     * curl -X POST http://localhost:8080/users \
     *   -H "Content-Type: application/json" \
     *   -d '{"upiId":"john@payflow","fullName":"John Doe","email":"john@example.com","phoneNumber":"9876543210"}'
     */
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * GET /users - Get all users
     *
     * Example curl:
     * curl -X GET http://localhost:8080/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * GET /users/{id} - Get user by ID
     *
     * Example curl:
     * curl -X GET http://localhost:8080/users/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /users/upi/{upiId} - Get user by UPI ID
     *
     * Example curl:
     * curl -X GET http://localhost:8080/users/upi/john@payflow
     */
    @GetMapping("/upi/{upiId}")
    public ResponseEntity<User> getUserByUpiId(@PathVariable String upiId) {
        return userService.findByUpiId(upiId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /users/email/{email} - Get user by email
     *
     * Example curl:
     * curl -X GET http://localhost:8080/users/email/john@example.com
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /users/balance-above/{amount} - Get users with balance above specified amount
     * This uses the custom JPQL query.
     *
     * Example curl:
     * curl -X GET http://localhost:8080/users/balance-above/500
     */
    @GetMapping("/balance-above/{amount}")
    public ResponseEntity<List<User>> getUsersWithBalanceAbove(@PathVariable BigDecimal amount) {
        List<User> users = userService.findUsersWithBalanceGreaterThan(amount);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}