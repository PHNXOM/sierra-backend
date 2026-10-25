package com.sierra.backend.repository;

import com.sierra.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("FROM User WHERE username = :username")
    User findByUsername(@Param("username") String username);

    @Query("FROM User WHERE phone IN :phones")
    List<User> findByPhoneIn(@Param("phones") List<String> phones);
}
