package com.project.volunteerOrganization.controller;

import com.project.volunteerOrganization.model.Chat;
import com.project.volunteerOrganization.repository.IChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatController
{
    @Autowired
    private IChatRepository repository;

    @PostMapping
    private ResponseEntity<?> sendChat(@RequestBody Chat chat)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            repository.save(chat);
            res.put("success",true);
            res.put("msg","Message sent successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Message sent failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/message/{sender}/{receiver}")
    private ResponseEntity<?> getMessage(@PathVariable Long sender,@PathVariable Long receiver)
    {
        HashMap<String, Object> res = new HashMap<>();
        try
        {
            List<Chat> chats = repository.getChatBySenderAndReceiver(sender, receiver);
            res.put("success", true);
            res.put("msg", chats);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the messages");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

}
