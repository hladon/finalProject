package com.repository;

import com.models.Message;

public class MessageDAO extends DAO<Message> {
    public MessageDAO() {
        type = Message.class;
    }
}
