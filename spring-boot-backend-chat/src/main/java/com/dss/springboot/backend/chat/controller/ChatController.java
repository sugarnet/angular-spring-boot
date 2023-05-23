package com.dss.springboot.backend.chat.controller;

import com.dss.springboot.backend.chat.domain.document.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Random;

@Controller
public class ChatController {

    private String[] colors = {"red", "green", "blue", "magenta", "purple", "orange"};

    @MessageMapping("/message")
    @SendTo("/chat/message")
    public Message receiveMessage(Message message) {
        message.setDate(new Date().getTime());

        if (message.getType().equals("NEW_USER")) {
            message.setText("Nuevo usuario");
            int random = new Random().nextInt(colors.length);
            message.setColor(colors[random]);
        }

        return message;

    }

    @MessageMapping("/writing")
    @SendTo("/chat/writing")
    public String isWriting(String username) {
        return username.concat(" está escribiendo...");
    }
}
