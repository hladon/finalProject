package com.repository;

import com.Exceptions.RepositoryException;
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

    public T findById(long id) throws RepositoryException {
        try{
        return entityManager.find(type, id);
        }catch (Exception e){
            throw new RepositoryException(e.getMessage());
        }
    }

    public T save(T item) throws Exception {
        try {
        entityManager.persist(item);
        return item;
        }catch (Exception e){
            throw new RepositoryException(e.getMessage());
        }
    }

    public String delete(T item) throws RepositoryException {
        try{
        entityManager.remove(item);
        return "Delete  done!";
        }catch (Exception e){
            throw new RepositoryException(e.getMessage());
        }
    }

    public T update(T item) throws RepositoryException {
        try {
        entityManager.merge(item);
        return item;
        }catch (Exception e){
            throw new RepositoryException(e.getMessage());
        }
    }
}
