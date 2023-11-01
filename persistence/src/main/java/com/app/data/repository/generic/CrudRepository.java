package com.app.data.repository.generic;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T item);

    T update(ID id, T item);

    List<T> saveAll(List<T> items);

    Optional<T> findById(ID id);

    List<T> findLast(int n);

    List<T> findAll();

    List<T> findAllById(List<ID> ids);

    T delete(ID id);

    List<T> deleteAllById(List<ID> ids);

    List<T> deleteAll();


    static <T> List<T> findAllWithMapping(Jdbi jdbi, String sql, RowMapper<T> mapper) {
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .map(mapper)
                .list());
    }

    static <T> T findOneWithMapping(Jdbi jdbi, String sql,int n, RowMapper<T> mapper) {
        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("n",n)
                .map(mapper)
                .list()
                .get(0));
    }



}
