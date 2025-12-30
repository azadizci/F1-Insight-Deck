package com.f1insight.repository;

import com.f1insight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Email'e göre kullanıcı bulma
     */
    Optional<User> findByEmail(String email);

    /**
     * Email'in mevcut olup olmadığını kontrol etme
     */
    boolean existsByEmail(String email);

    /**
     * Kullanıcıyı favori pilot ile birlikte getirme
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoriteDriver WHERE u.id = :id")
    Optional<User> findByIdWithFavoriteDriver(@Param("id") Long id);
}
