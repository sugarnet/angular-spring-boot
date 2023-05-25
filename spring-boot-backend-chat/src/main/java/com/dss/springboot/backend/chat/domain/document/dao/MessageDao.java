package com.dss.springboot.backend.chat.domain.document.dao;

import com.dss.springboot.backend.chat.domain.document.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageDao extends MongoRepository<Message, String> {
    List<Message> findFirst10ByOrderByDateDesc();
}
