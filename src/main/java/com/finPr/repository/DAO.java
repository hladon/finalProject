package com.finPr.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
@Transactional
public class DAO<T> {
    @PersistenceContext
    protected EntityManager entityManager;

    Class<T> type;

    public T findById(int id) {
        return entityManager.find(type, id);
    }

    public String save(T item) {
        entityManager.persist(item);
        return "Save  done";
    }

    public String delete(T item) {
        entityManager.remove(item);
        return "Delete  done!";
    }

    public String update(T item) {
        entityManager.merge(item);
        return "Update  done";
    }
}
