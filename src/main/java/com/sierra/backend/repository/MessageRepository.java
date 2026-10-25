package com.sierra.backend.repository;

import com.sierra.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("FROM Message WHERE (sender.id = :userId1 AND receiver.id = :userId2) " +
            "OR (sender.id = :userId2 AND receiver.id = :userId1) " +
            "ORDER BY sentAt ASC")
    List<Message> getMessagesBetweenTwoUsers(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);


    @Modifying
    @Query("DELETE FROM Message WHERE (sender.id = :userId1 AND receiver.id = :userId2)" +
            "OR (sender.id = :userId2 AND receiver.id = :userId1)")
    void deleteAllMessagesBetweenTwoUsers(@Param("userId1") UUID userId1,@Param("userId2") UUID userId2);


    @Modifying
    @Query("DELETE FROM Message WHERE sender.id = :userId OR receiver.id = :userId")
    void deleteAllMessagesByUserId(@Param("userId") UUID userId);


    @Query("SELECT COUNT(*) FROM Message WHERE sender.id = :otherUserId AND receiver.id = :currentUserId AND isRead = false")
    int getUnreadCount(@Param("currentUserId") UUID currentUserId, @Param("otherUserId") UUID otherUserId);


    @Query(value = """
            WITH sub AS (
                SELECT *,
                       ROW_NUMBER() OVER (
                           PARTITION BY CASE
                               WHEN m.sender_id = :userId THEN m.receiver_id
                               ELSE m.sender_id
                           END
                           ORDER BY m.sent_at DESC
                       ) AS rnk
                FROM messages m
                WHERE m.sender_id = :userId OR m.receiver_id = :userId
            )
            SELECT *
            FROM sub
            WHERE rnk = 1
            ORDER BY sent_at DESC;""", nativeQuery = true)
    List<Message> getLatestMessagesByUserId(@Param("userId") UUID userId);


    @Modifying
    @Query("UPDATE Message SET isRead = true WHERE sender.id = :otherUserId AND receiver.id = :currentUserId AND isRead = false")
    void markMessagesAsRead(@Param("currentUserId") UUID currentUserId, @Param("otherUserId") UUID otherUserId);
}