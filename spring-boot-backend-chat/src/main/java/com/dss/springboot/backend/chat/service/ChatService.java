package com.dss.springboot.backend.chat.service;

import com.dss.springboot.backend.chat.domain.document.Message;

import java.util.List;

public interface ChatService {
    List<Message> getLast10Messages();
    Message save(Message message);
}
