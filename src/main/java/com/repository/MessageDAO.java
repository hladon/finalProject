package com.repository;

import com.Exceptions.InternalServerError;
import com.models.Message;
import com.models.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessageDAO extends DAO<Message> {
    public MessageDAO() {
        type = Message.class;
    }

    private static final String GET_MESSAGES = "SELECT * FROM MESSAGE WHERE DATE_DELETED IS NULL AND " +
            " ?1 IN (USER_TO,USER_FROM) AND ?2 IN (USER_TO,USER_FROM) AND  ID<?3 AND ROWNUM<= 20 ORDER BY DATE_SEND DESC ";
    private static final String DELETE_ALL_MESSAGES = "UPDATE MESSAGE SET DATE_DELETED=?1 WHERE DATE_DELETED IS NULL AND " +
            " ?2 IN (USER_TO,USER_FROM) AND ?3 IN (USER_TO,USER_FROM)  ";
    private static final String LAST_DIALOGS = "SELECT * FROM USER_PROFILE WHERE ID IN (SELECT DISTINCT(USER_FROM) " +
            "FROM MESSAGE WHERE USER_TO=?1 AND DATE_DELETED IS NULL " +
            "UNION SELECT DISTINCT(USER_TO) FROM MESSAGE WHERE USER_FROM=?1 AND DATE_DELETED IS NULL) ";

    public List<User> getUserWithDialogs(Long userId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(LAST_DIALOGS, User.class)
                    .setParameter(1, userId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Message> getNextMessages(Long userId, Long userFromId, Long lastMessageId) throws InternalServerError {
        try {
            if (lastMessageId == null)
                lastMessageId = Long.MAX_VALUE;
            return entityManager.createNativeQuery(GET_MESSAGES, Message.class)
                    .setParameter(1, userId)
                    .setParameter(2, userFromId)
                    .setParameter(3, lastMessageId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public void deleteDialog(Long userId, Long friendId) throws InternalServerError {
        try {
            entityManager.createNativeQuery(DELETE_ALL_MESSAGES, Message.class)
                    .setParameter(1, new Date())
                    .setParameter(2, friendId)
                    .setParameter(3, userId)
                    .executeUpdate();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }


}
