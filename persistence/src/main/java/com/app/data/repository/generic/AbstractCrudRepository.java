package com.app.data.repository.generic;

import com.app.model.user.Role;
import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {
    protected final Jdbi jdbi;

    @SuppressWarnings("unchecked")
    private final Class<T> entityType
            = (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public T save(T item) {
        var sql = "insert into %s %s values %s;".formatted(
                tableName(),
                columnNamesForInsert(),
                columnValuesForInsert(item)
        );
        var insertedRows = jdbi.withHandle(handle -> handle.execute(sql));

        if (insertedRows == 0) {
            throw new IllegalStateException("Row not inserted");
        }

        return findLast(1).get(0);
    }

    @Override
    public T update(ID id, T item) {
        var sql = "update %s set %s where id = :id".formatted(
                tableName(),
                columnNamesAndValuesForUpdate(item)
        );
        var updatedRows = jdbi.withHandle(handle -> handle
                .createUpdate(sql)
                .bind("id", id)
                .execute());

        if (updatedRows == 0) {
            throw new IllegalStateException("Update not completed");
        }

        return findById(id).orElseThrow();
    }

    @Override
    public List<T> saveAll(List<T> items) {
        var sql = "insert into %s %s values %s".formatted(
                tableName(),
                columnNamesForInsert(),
                items
                        .stream()
                        .map(this::columnValuesForInsert)
                        .collect(joining(", ")));
        var insertedRows = jdbi.withHandle(handle -> handle.execute(sql));
        if (insertedRows == 0) {
            throw new IllegalStateException("Rows not inserted");
        }
        return findLast(insertedRows);
    }

    @Override
    public Optional<T> findById(ID id) {
        var sql = "select * from " + tableName() + " where id = :id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("id", id)
                .mapToBean(entityType)
                .findFirst()
        );
    }

    @Override
    public List<T> findLast(int n) {
        var sql = "select * from " + tableName() + " order by id desc limit :n";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("n", n)
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public List<T> findAll() {
        var sql = "select * from " + tableName();
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
    }

    @Override
    public List<T> findAllById(List<ID> ids) {
        var sql = "select * from " + tableName() + " where id in (<ids>)";
        var items = jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bindList("ids", ids)
                .mapToBean(entityType)
                .list());

        if (items.size() != ids.size()) {
            throw new IllegalStateException("Not all ids are present in table");
        }

        return items;
    }

    @Override
    public T delete(ID id) {
        var itemToDelete = findById(id)
                .orElseThrow(() -> new IllegalStateException("No item to delete"));

        var sql = "delete from " + tableName() + " where id = :id";
        jdbi.useHandle(handle -> handle
                .createUpdate(sql).bind("id", id)
                .execute());
        return itemToDelete;
    }

    @Override
    public List<T> deleteAllById(List<ID> ids) {
        var items = findAllById(ids);
        var sql = "delete from " + tableName() + " where id in (<ids>)";
        jdbi.useHandle(handle -> handle
                .createUpdate(sql)
                .bindList("ids", ids)
                .execute());
       return items;
    }

    @Override
    public List<T> deleteAll() {
        var items = findAll();
        var sql = "delete from " + tableName() + " where id > 0";
        jdbi.useHandle(handle -> handle.execute(sql));
        return items;
    }


    // ----------------------------------------------------------------------
    // METODY POMOCNICZE
    // ----------------------------------------------------------------------

    private String toLowerUnderscore(String upperCamel) {
        return CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE,
                upperCamel);
    }

    private String tableName() {
        return English.plural(toLowerUnderscore(removeDbIfExist(entityType.getSimpleName())));
    }

    private String removeDbIfExist(String tableName){
        return tableName.replaceAll("(?i)Db", "");
    }

    private String[] columnNamesForSelect() {
        return Arrays
                .stream(entityType.getDeclaredFields())
                .map(field -> toLowerUnderscore(field.getName()))
                .toArray(String[]::new);
    }

    private String columnNamesForInsert() {
        var cols = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> toLowerUnderscore(field.getName()))
                .collect(joining(", "));
        return "( %s )".formatted(cols);
    }

    private String columnValuesForInsert(T item) {
        var values = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (field.get(item) == null) {
                            return "NULL";
                        }

                        if (List.of(
                                String.class,
                                Enum.class,
                                LocalDateTime.class,
                                Role.class,
                                LocalDate.class).contains(field.getType())) {
                            return "'%s'".formatted(field.get(item));
                        }
                        return field.get(item).toString();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).collect(joining(", "));
        return "( %s )".formatted(values);
    }

    private String columnNamesAndValuesForUpdate(T item) {
        return Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return !field.getName().equalsIgnoreCase("id") && field.get(item) != null;
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (List.of(
                                String.class,
                                Enum.class,
                                LocalDateTime.class,
                                Role.class,
                                LocalDate.class).contains(field.getType())) {
                            return "%s='%s'".formatted(
                                    toLowerUnderscore(field.getName()),
                                    field.get(item));
                        }
                        return toLowerUnderscore(field.getName()) + "=" + field.get(item).toString();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).collect(joining(", "));
    }
}
