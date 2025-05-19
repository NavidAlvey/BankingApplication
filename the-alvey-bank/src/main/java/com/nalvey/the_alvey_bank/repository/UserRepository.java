// Provides methods to interact with database
package com.nalvey.the_alvey_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nalvey.the_alvey_bank.entity.User;

// Bind repository to particular entity (ie. User)
// Also provide information about primary key for DB
// JpaRepository is an interface:
// Class extends to class & interface extends to interface
// Therefore, make UserRepository an interface and extend it to JpaRepository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);

}
