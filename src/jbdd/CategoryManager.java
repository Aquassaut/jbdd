package jbdd;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryManager implements Queryable<CategoryBean> {
    public static final String TABLE_NAME = "category";
    public static final String CREATE_STMT = "" +
            "create table " + TABLE_NAME + " (" +
            "id_" + TABLE_NAME + " serial," +
            "name_" + TABLE_NAME + " varchar(20));";

    @Override
    public int create(Connection conn, CategoryBean table) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CategoryBean read(Connection conn, int key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<CategoryBean> readAll(Connection conn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Connection conn, int key, CategoryBean table) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delete(Connection conn, int key) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
