package org.effectivemobile.services;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface Service <T, ID> {
    T createEntity(T entity);
    Optional<T> getEntityById(ID id);
    void updateEntity(T entity);
    void deleteEntityById(ID id);
    List<T> getAllEntities();
}
