package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    //create a new message find the posted_by
    @Query("SELECT a.posted_by FROM Message a WHERE a.posted_by = :postedBy")
    Optional<Message> getPostedBy(@Param("postedBy") int id);

    //get all message by a particular user (posted_by)
    @Query("SELECT a FROM Message a WHERE a.posted_by = :postedBy")
    List<Message> findAllByPostedBy(@Param("postedBy") int id);
    
    //get singular message by id
    @Query(value ="SELECT * FROM Message a where a.message_id = ?1", nativeQuery = true)
    Message getMessageById(int id);

    //Delete the message and get the row count
    @Modifying
    @Query(value = "DELETE FROM Message a WHERE a.message_id = ?1", nativeQuery = true)
    Integer deleteByMessageId(int id);

    //get the message text to be updated
    @Query("SELECT a.message_text FROM Message a WHERE a.message_text = :text")
    Optional<Message> getText(@Param("text") String text);

    //Update the message
    @Modifying
    @Query(value = "UPDATE Message a SET a.message_text = ?1 WHERE a.message_id = ?2", nativeQuery = true)
    int updateMessage(String text, int id);

} //end MessageRepository
