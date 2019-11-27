package com.repository;

import com.Exceptions.InternalServerError;
import com.models.Message;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessageDAO extends DAO<Message> {
    public MessageDAO() {
        type = Message.class;
    }

    private static final String GET_MESSAGES = "SELECT * FROM MESSAGE WHERE DATE_DELETED IS NULL AND " +
            " ?1 IN (USER_TO,USER_FROM) AND ?2 IN (USER_TO,USER_FROM) AND  DATE_SEND<?3 AND ROWNUM<= 20 ORDER BY DATE_SEND DESC ";
    private static final String DELETE_ALL_MESSAGES = "UPDATE MESSAGE SET DATE_DELETED=?1 WHERE  ?2 IN (USER_TO,USER_FROM)";
    private static final String DELETE_MESSAGE = "UPDATE MESSAGE SET DATE_DELETED=?1 WHERE  ID=?2";
    private static final String LAST_MESSAGES="SELECT * FROM MESSAGE WHERE DATE_DELETED IS NULL AND USER_TO=?1 GROUP BY " +
            "GROUP BY ";

    public List<Message> getDialogsMessages(Long userId) throws InternalServerError{
        try{

        }catch (Exception e){
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Message> getNextMessages(Long userId, Long userFromId, Date lastMessageDate) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_MESSAGES, Message.class)
                    .setParameter(1, userId)
                    .setParameter(2, userFromId)
                    .setParameter(3, lastMessageDate)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void deleteAllMessages(Long userFromId) throws InternalServerError {
        try {
             entityManager.createNativeQuery(DELETE_ALL_MESSAGES, Message.class)
                    .setParameter(1, new Date())
                    .setParameter(2, userFromId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void deleteMessage(Long messageId,Date dateDeleted) throws InternalServerError {
        try {
            entityManager.createNativeQuery(DELETE_MESSAGE, Message.class)
                    .setParameter(1, dateDeleted)
                    .setParameter(2, messageId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}
