package com.repository;

import com.Exceptions.InternalServerError;
import com.models.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDAO extends DAO<Message> {
    public MessageDAO() {
        type = Message.class;
    }

    private static final String GET_INCOME_MESSAGES = "SELECT * FROM MESSAGE WHERE DATE_DELETED IS NULL AND " +
            "USER_TO=?1 AND ROWNUM<= 10 ORDER BY DATE_SEND ";
    private static final String GET_OUTCOME_MESSAGES = "SELECT * FROM MESSAGE WHERE DATE_DELETED IS NULL AND " +
            "USER_FROM=?1 AND ROWNUM<= 10 ORDER BY DATE_SEND ";

    public List<Message> getIncomeMessages(Long userId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_INCOME_MESSAGES, Message.class)
                    .setParameter(1, userId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }

    }

    public List<Message> getOutcomeMessages(Long userId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_OUTCOME_MESSAGES, Message.class)
                    .setParameter(1, userId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }

    }
}
