package service;


import java.util.List;
import java.util.Optional;

public interface Service <T, ID> {
    boolean createEntity(T entity);
    Optional<T> getEntityById(ID id);
    void updateEntity(T entity);
    void deleteEntityById(ID id);
    List<T> getAllEntities();
}
