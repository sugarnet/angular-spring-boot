package com.dss.springboot.backend.chat.service;

import com.dss.springboot.backend.chat.domain.document.Message;
import com.dss.springboot.backend.chat.domain.document.dao.MessageDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final MessageDao messageDao;

    public ChatServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<Message> getLast10Messages() {
        return messageDao.findFirst10ByOrderByDateDesc();
    }

    @Override
    public Message save(Message message) {
        return messageDao.save(message);
    }
}
