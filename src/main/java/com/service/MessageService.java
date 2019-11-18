package com.service;

import com.Exceptions.ExceedLimits;
import com.models.FriendshipStatus;
import com.models.Message;
import com.repository.MessageDAO;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RelationshipDAO relationshipDAO;

    public Message sendMessage(String text, Long idFrom, Long idTo) throws Exception {
        Message message = new Message();
        if (text.length() > 140 || !relationshipDAO.getRelationship(idFrom, idTo).getRelates().equals(FriendshipStatus.FRIEND))
            throw new ExceedLimits();
        Date date = new Date();
        message.setText(text);
        message.setDateEdited(date);
        message.setDateSent(date);
        message.setUserFrom(userDao.findById(idFrom));
        message.setUserTo(userDao.findById(idTo));
        messageDAO.save(message);
        return message;
    }

    public void deleteMessage(Long id, Long userId) throws Exception {
        Message message = messageDAO.findById(id);
        if (message.getUserFrom().getId() != userId)
            throw new ExceedLimits();
        message.setDateDeleted(new Date());
        messageDAO.update(message);
    }

    public Message editeMessage(String text, Long id, Long userId) throws Exception {
        Message message = messageDAO.findById(id);
        if (message.getUserFrom().getId() != userId)
            throw new ExceedLimits();
        message.setText(text);
        message.setDateEdited(new Date());
        return messageDAO.update(message);
    }

    public Message readMessage(Long id) throws Exception {
        Message message = messageDAO.findById(id);
        if (message.getDateRead() != null)
            return null;
        message.setDateRead(new Date());
        messageDAO.save(message);
        return message;
    }


}
