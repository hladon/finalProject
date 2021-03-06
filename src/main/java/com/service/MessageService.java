package com.service;

import com.Exceptions.ExceedLimits;
import com.Exceptions.NotAuthorized;
import com.Exceptions.NotFriend;
import com.models.FriendshipStatus;
import com.models.Message;
import com.models.User;
import com.repository.MessageDAO;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RelationshipDAO relationshipDAO;

    public List<User> getUserWithDialogs(User user) throws Exception {
        return messageDAO.getUserWithDialogs(user.getId());
    }

    public List<Message> getDialog(Long userId, Long friendId, Long lastMessageId) throws Exception {
        return messageDAO.getNextMessages(userId, friendId, lastMessageId);
    }


    public Message sendMessage(Message message, User user, Long idTo) throws Exception {
        if (message.getText().length() > 140)
            throw new ExceedLimits();
        if (!relationshipDAO.getRelationship(user.getId(), idTo).getRelates().equals(FriendshipStatus.FRIEND))
            throw new NotFriend();
        Date date = new Date();
        message.setDateEdited(date);
        message.setDateSent(date);
        message.setUserFrom(user);
        message.setUserTo(userDao.findById(idTo));
        messageDAO.save(message);
        return message;
    }

    public void deleteDialog(Long userId, Long friendId) throws Exception {
        messageDAO.deleteDialog(userId, friendId);
    }

    @Transactional
    public void deleteMessage(long[] ids, User user) throws Exception {
        if (ids.length > 10)
            throw new ExceedLimits();
        for (long id : ids) {
            Message message = messageDAO.findById(id);
            if (!message.getUserFrom().equals(user))
                throw new NotAuthorized();
            message.setDateDeleted(new Date());
            messageDAO.update(message);
        }
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
