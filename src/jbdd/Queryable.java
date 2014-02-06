package jbdd;

import java.sql.Connection;
import java.util.List;

public interface Queryable<T> {
    public abstract int create(Connection conn, T table);
    public abstract T read(Connection conn, int key);
    public abstract List<T> readAll(Connection conn);
    public abstract int update(Connection conn, int key, T table) throws ArticleNotAvailableException;
    public abstract int delete(Connection conn, int key);
    public boolean createTable(Connection conn);
}
