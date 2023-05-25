package com.dss.springboot.backend.chat.controller;

import com.dss.springboot.backend.chat.domain.document.Message;
import com.dss.springboot.backend.chat.domain.document.dao.MessageDao;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Random;

@Controller
public class ChatController {

    private String[] colors = {"red", "green", "blue", "magenta", "purple", "orange"};
    private final MessageDao messageDao;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(MessageDao messageDao, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageDao = messageDao;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/chat/message")
    public Message receiveMessage(Message message) {
        message.setDate(new Date().getTime());

        if (message.getType().equals("NEW_USER")) {
            message.setText("Nuevo usuario");
            int random = new Random().nextInt(colors.length);
            message.setColor(colors[random]);
        } else {
            messageDao.save(message);
        }

        return message;

    }

    @MessageMapping("/writing")
    @SendTo("/chat/writing")
    public String isWriting(String username) {
        return username.concat(" está escribiendo...");
    }

    @MessageMapping("/history")
    public void history(String clientId) {
        simpMessagingTemplate.convertAndSend("/chat/history/" + clientId, messageDao.findFirst10ByOrderByDateDesc());
    }
}
