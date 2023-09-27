package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.*;

@Transactional
@Service
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /*
     * create a new message
     * message_text cannot be blank and is under 255 characters
     * posted_by refers to a real, existing user
     */
    public Message addMessage(Message message) {
        Optional<Message> user = messageRepository.getPostedBy(message.getPosted_by());
        if(message.getMessage_id() == null && !message.getMessage_text().isBlank() && message.getMessage_text().length() < 255 && user.isPresent()) {
            return messageRepository.save(message);
        }
        return null;
    }

    /*
     * retrieve all messages
     */
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    /*
     * retrieve message by message_id
     */
    public Message getMessageById(int id) {
        if(messageRepository.findById(id).isPresent()) {
            return messageRepository.findById(id).get();
        }
        return null;
    }

    /*
     * delete a message by message_id
     * should remove an existing message from the database
     */
    public Integer deleteMessageById(int id) {
        if(messageRepository.findById(id).isPresent()) {
            return messageRepository.deleteByMessageId(id);
        }
        return 0;
    }

    /*
     * update a message by message_id
     * only if the message_id already exists
     * new message_text is not blank/empty
     * and is not over 255 characters
     */
    public Integer updateMessageById(int id, Message message) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        Optional<Message> optionalText = messageRepository.getText(message.getMessage_text());
        if(optionalMessage.isPresent() && !optionalText.isEmpty() && message.getMessage_text().length() < 255) {
            messageRepository.updateMessage(message.getMessage_text(), id);
            return 1;
        }
        return 0;
    }

    /*
     * retrieve all message written by a particular user
     * or aka posted_by
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        return messageRepository.findAllByPostedBy(id);

    }
    
} //end MessageService
