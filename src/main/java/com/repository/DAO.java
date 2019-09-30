package com.repository;

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

    public T findById(long id) {
        return entityManager.find(type, id);
    }

    public T save(T item) {
        entityManager.persist(item);
        return item;
    }

    public String delete(T item) {
        entityManager.remove(item);
        return "Delete  done!";
    }

    public T update(T item) {
        entityManager.merge(item);
        return item;
    }
}
