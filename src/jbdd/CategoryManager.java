package jbdd;

import java.sql.Connection;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategoryManager implements Queryable<CategoryBean> {
    private static final String CREATE_STMT = "" +
            "create table category ( " +
            "category_id serial PRIMARY KEY," +
            "category_name varchar(20))";

    private PreparedStatement _pstm;
    private ResultSet _rs;

    public boolean createTable(Connection conn) {
        String lastStep = "Before connection";
        try {
            String sql = "drop table if exists category cascade";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table";
            _pstm.execute();

            _pstm = conn.prepareStatement(CREATE_STMT);
            lastStep = "recreating table";
            _pstm.execute();

        } catch (Exception e) {
            System.err.println("Problem encountered creating category table");
            System.err.println(lastStep);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int create(Connection conn, CategoryBean table) {
        int res = -1;
        try {
            String sql = "insert into category (category_name) values (?)";
            _pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            _pstm.setString(1, table.get_name());


            _pstm.executeUpdate();
            _rs = _pstm.getGeneratedKeys();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to insert new category in the database");
            }
            res = _rs.getInt(1); //la premiere colonne est l'id
            table.set_id(res);
        } catch (Exception e) {
            System.err.println("Problem encountered inserting a category");
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public CategoryBean read(Connection conn, int key) {
        CategoryBean table = new CategoryBean();
        try {
            String sql = "select category_id, category_name " +
                    "from category " +
                    "where category_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            _rs = _pstm.executeQuery();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to read category number " + key);
            }
            table.set_id(_rs.getInt(1));
            table.set_name(_rs.getString(2));
        } catch (Exception e) {
            System.err.println("Problem encountered reading a category");
            e.printStackTrace();
        }
        return table;
    }

    @Override
    public List<CategoryBean> readAll(Connection conn) {
        ArrayList<CategoryBean> clients = null;
        try {
            String sql = "select category_id, category_name " +
                    "from category ";
            _pstm = conn.prepareStatement(sql);
            _rs = _pstm.executeQuery();

            if (! _rs.next()) {
                throw new RuntimeException("Failed to read all category");
            }
            clients = new ArrayList<CategoryBean>();
            do {
                CategoryBean table = new CategoryBean();
                table.set_id(_rs.getInt(1));
                table.set_name(_rs.getString(2));
                clients.add(table);
            } while (_rs.next());
        } catch (Exception e) {
            System.err.println("Problem encountered getting all categories");
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public int update(Connection conn, int key, CategoryBean table) {
        int affected = -1;
        try {
            String sql = "update category set " +
                         "category_id = ?," +
                         "category_name = ? " +
                         "where category_id = ?";
            _pstm = conn.prepareStatement(sql);
            int i = 0;
            _pstm.setInt(++i, table.get_id());
            _pstm.setString(++i, table.get_name());
            _pstm.setInt(++i, key);
            affected = _pstm.executeUpdate();
        } catch (Exception e) {
            System.err.println("Problem encountered updating a category");
            e.printStackTrace();
        }
        return affected;
    }

    @Override
    public int delete(Connection conn, int key) {
        int affected = -1;
        try {
            String sql = "delete from category " +
                         "where category_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);
            affected = _pstm.executeUpdate();
        } catch (Exception e) {
            System.err.println("Problem encountered deleting a category");
            e.printStackTrace();
        }
        return affected;
    }
}
