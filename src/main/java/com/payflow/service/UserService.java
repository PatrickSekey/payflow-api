// src/main/java/com/payflow/service/UserService.java
package com.payflow.service;

import com.payflow.entity.User;
import com.payflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * UserService handles all business logic related to User operations.
 *
 * Spring Boot's dependency injection at startup:
 * 1. Spring scans the classpath for components marked with @Service, @Repository, @Controller
 * 2. When it finds this class, it creates an instance (bean) managed by the Spring container
 * 3. The @Autowired annotation on the constructor tells Spring to inject the required dependency
 * 4. Spring finds the UserRepository bean and injects it into this service
 * 5. This is constructor injection - the recommended way for dependency injection
 *
 * The @Transactional annotation ensures that database operations are executed within a transaction
 * which provides ACID properties (Atomicity, Consistency, Isolation, Durability)
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injection - Spring automatically injects the UserRepository bean.
     * This is the recommended approach for dependency injection because:
     * - Makes dependencies explicit and visible
     * - Makes the class easier to test (can pass mock repositories)
     * - Prevents null pointer exceptions
     * - Works well with final fields (immutability)
     *
     * At startup, Spring:
     * 1. Creates a proxy for UserRepository
     * 2. Creates an instance of UserService
     * 3. Injects the UserRepository proxy into the constructor
     * 4. Makes the UserService available for injection into other beans
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the system.
     * Validates that the UPI ID is unique before saving.
     */
    public User registerUser(User user) {
        // Check if UPI ID already exists
        if (userRepository.existsByUpiId(user.getUpiId())) {
            throw new RuntimeException("User with UPI ID " + user.getUpiId() + " already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        // Initialize balance to zero if not set
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }

        // Save the user to the database
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the system.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a user by their UPI ID.
     * This uses the custom derived query method.
     */
    public Optional<User> findByUpiId(String upiId) {
        return userRepository.findByUpiId(upiId);
    }

    /**
     * Retrieves a user by their email.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds users with balance greater than a specified amount.
     * This uses the custom JPQL query.
     */
    public List<User> findUsersWithBalanceGreaterThan(BigDecimal amount) {
        return userRepository.findUsersWithBalanceGreaterThan(amount);
    }

    /**
     * Updates a user's balance.
     * Used internally by the transaction service.
     */
    public User updateUserBalance(Long userId, BigDecimal newBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setBalance(newBalance);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }
}