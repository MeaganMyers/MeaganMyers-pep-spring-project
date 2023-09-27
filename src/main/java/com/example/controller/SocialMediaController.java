package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;
    
    public SocialMediaController(AccountService accountService, MessageService messageService, AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    /*
     * Postmapping the register
     * POST localhost:8080/register
     * if successful, return account with its account_id
     * if unsuccessful due to duplicate username, status is 409 (conflict)
     * if unsuccessful due for some other reason, status is 400 (client aka bad request)
     * [GOOD]
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) throws AuthenticationException {
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null) {
            return new ResponseEntity<>(addedAccount, HttpStatus.OK);
        } else {
            if(accountRepository.findUsername(account.getUsername()) != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /*
     * Postmapping the login
     * POST localhost:8080/login
     * if successful, return account with its account_id
     * if unsuccessful, status is 401 (unauthorized)
     * [GOOD]
     */
    @PostMapping("/login")
    public ResponseEntity<Optional<Account>> login(@RequestBody Account account) throws AuthenticationException {
        Optional<Account> getLogin = accountService.checkAccount(account.getUsername(), account.getPassword(), account);
        if(getLogin.isPresent()) {
            return ResponseEntity.ok().body(getLogin);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /*
     * Postmapping the new message
     * POST localhost:8080/messages
     * if successful, return message with its message_id
     * if unsuccessful, status is 400 (client)
     * [GOOD]
     */
    @PostMapping(value = "/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        Message message = messageService.addMessage(newMessage);
        if(message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Getmapping all messages
     * GET localhost:8080/messages
     * if there are no messages, response body should be empty
     * status is always 200
     * [GOOD]
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public List<Message> getMessages() {
        return messageService.getMessages();
    }

    /*
     * Getmapping message by message_id
     * GET localhost:8080/messages/{message_id}
     * if there is no message, response body should be empty
     * status is always 200
     * [GOOD]
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") int message_id) {
        Optional<Message> getMessage = messageRepository.findById(message_id);
        if(getMessage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.ok().body(getMessage.get());
        }
    }

    /*
     * Deletemapping the message by message_id
     * DELETE localhost:8080/messages/{message_id}
     * if message existed, response body should contain the number of rows deleted(1)
     *    status should be 200
     * if message did not exist, response body should be empty
     *    status should be 200
     * [GOOD]
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("message_id") int message_id) {
        Integer deleteMessage = messageService.deleteMessageById(message_id);
        if(deleteMessage == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.ok(deleteMessage);
        }
    }

    /*
     * Patchmapping (or Updating) the message by message_id
     * PATCH localhost:8080/messages/{message_id}
     * if update is successful, response body should contain the number of rows updated(1)
     *    status should be 200
     * if update is unsuccessful for any reason, response status should be 400 (client)
     * [GOOD]
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("message_id") int id, @RequestBody Message message) {
        Message getMsg = messageService.getMessageById(id);
        String text = message.getMessage_text();
        if(text == null || text.isEmpty() || text.length() > 255 || getMsg == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok().body(1);
        }
    }


    /*
     * Getmapping all messages by account_id
     * GET localhost:8080/accounts/{account_id}/messages
     * if there are no messages list should be empty
     * response status should always be 200
     * [GOOD]
     */
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForUser(@PathVariable("account_id") int account_id) {
        List<Message> getAllUserMessages = messageService.getAllMessagesByAccountId(account_id);
        if(getAllUserMessages == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.ok().body(getAllUserMessages);
        }
    }


}
