package org.example.mediserve.repository;

import jakarta.transaction.Transactional;
import org.example.mediserve.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByChatId(Long chatId);

    @Transactional
    @Modifying
    @Query("delete users u where u.chatId=:chatId")
    void deleteUserByChatId(@Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("update users u set u.firstName=:firstName where u.chatId=:chatId")
    void updateFirstName(@Param("firstName") String firstName, @Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("update users u set u.lastName=:lastName where u.chatId=:chatId")
    void updateLastName(@Param("lastName") String lastName, @Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("update users u set u.username=:username where u.chatId=:chatId")
    void updateUserName(@Param("username") String username, @Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("update users u set u.password=:password where u.chatId=:chatId")
    void updatePassword(@Param("password") String password, @Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("update users u set u.experience=:experience where u.chatId=:chatId")
    void updateExperience(@Param("experience") String experience, @Param("chatId") Long chatId);

}
