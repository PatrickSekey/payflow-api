// src/main/java/com/payflow/repository/UserRepository.java
package com.payflow.repository;

import com.payflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository provides CRUD operations and custom queries for User entities.
 * Extending JpaRepository provides standard methods like save(), findById(), findAll(), etc.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Derived query method that finds a user by their UPI ID.
     * JPA automatically generates SQL based on the method name.
     * The generated SQL would be: SELECT * FROM user WHERE upi_id = ?
     * The '?' is a placeholder that gets replaced with the actual parameter value.
     */
    Optional<User> findByUpiId(String upiId);

    /**
     * Custom JPQL query to find users with balance greater than a specified amount.
     * JPQL (Java Persistence Query Language) uses entity names and field names,
     * not table and column names.
     * This is more portable than native SQL.
     */
    @Query("SELECT u FROM User u WHERE u.balance > :amount")
    List<User> findUsersWithBalanceGreaterThan(@Param("amount") BigDecimal amount);

    /**
     * Derived query method to find users by email.
     * JPA automatically generates: SELECT * FROM user WHERE email = ?
     */
    Optional<User> findByEmail(String email);

    /**
     * Derived query method to check if a UPI ID exists.
     * JPA generates: SELECT COUNT(*) FROM user WHERE upi_id = ?
     */
    boolean existsByUpiId(String upiId);
}