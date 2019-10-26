package com.repository;

import com.Exceptions.InternalServerError;
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

    public T findById(long id) throws InternalServerError {
        try{
        return entityManager.find(type, id);
        }catch (Exception e){
            throw new InternalServerError(e.getMessage());
        }
    }

    public T save(T item) throws Exception {
        try {
        entityManager.persist(item);
        return item;
        }catch (Exception e){
            throw new InternalServerError(e.getMessage());
        }
    }

    public String delete(T item) throws InternalServerError {
        try{
        entityManager.remove(item);
        return "Delete  done!";
        }catch (Exception e){
            throw new InternalServerError(e.getMessage());
        }
    }

    public T update(T item) throws InternalServerError {
        try {
        entityManager.merge(item);
        return item;
        }catch (Exception e){
            throw new InternalServerError(e.getMessage());
        }
    }
}
