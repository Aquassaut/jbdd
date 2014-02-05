package jbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ArticleManager implements Queryable<ArticleBean> {

    private PreparedStatement _pstm;
    private ResultSet _rs;

    public static final String CREATE_STMT = "" +
            "create table article ( " +
            "article_id serial PRIMARY KEY, " +
            "article_name varchar(20)," +
            "article_availability int, " +
            "article_price real, " +
            "category_id integer references category(category_id))";

    public boolean createTable(Connection conn) {
        boolean success;
        String lastStep = "Before connection";
        try {
            String sql = "drop table if exists article cascade";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table";
            _pstm.execute();

            _pstm = conn.prepareStatement(CREATE_STMT);
            lastStep = "recreating table";
            success = _pstm.execute();
        } catch (Exception e) {
            System.err.println("Problem encountered creating article table");
            System.err.println(lastStep);
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public int create(Connection conn, ArticleBean table) {
        int res = -1;
        try {
            String sql = "insert into article (article_name, " + 
                "article_availability, article_price, category_id) " +
                "values (?, ?, ?, ?)";
            _pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;
            _pstm.setString(++i, table.get_name());
            _pstm.setInt(++i, table.get_availability());
            _pstm.setBigDecimal(++i, table.get_price());
            _pstm.setInt(++i, table.get_idCategory());

            _pstm.executeUpdate();
            _rs = _pstm.getGeneratedKeys();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to insert new article in the database");
            }
            res = _rs.getInt(1); //la premiere colonne est l'id
            table.set_id(res);
        } catch (Exception e) {
            System.err.println("Problem encountered inserting an article");
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public ArticleBean read(Connection conn, int key) {
        ArticleBean table = new ArticleBean();
        try {
            String sql = "select article_id, article_name, " +
                "article_availability, article_price, category_id " +
                "from article " +
                "where article_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            _rs = _pstm.executeQuery();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to read article number " + key);
            }
            table.set_id(_rs.getInt(1));
            table.set_name(_rs.getString(2));
            table.set_availability(_rs.getInt(3));
            table.set_price(_rs.getBigDecimal(4));
            table.set_idCategory(_rs.getInt(5));
        } catch (Exception e) {
            System.err.println("Problem encountered reading an article");
            e.printStackTrace();
        }
        return table;
    }

    @Override
    public List<ArticleBean> readAll(Connection conn) {
        ArrayList<ArticleBean> clients = null;
        try {
            String sql = "select article_id, article_name, " +
                "article_availability, article_price, category_id " +
                "from article order by article_price";
            _pstm = conn.prepareStatement(sql);
            _rs = _pstm.executeQuery();

            if (! _rs.next()) {
                throw new RuntimeException("Failed to read all article");
            }
            clients = new ArrayList<ArticleBean>();
            do {
                ArticleBean table = new ArticleBean();
                table.set_id(_rs.getInt(1));
                table.set_name(_rs.getString(2));
                table.set_availability(_rs.getInt(3));
                table.set_price(_rs.getBigDecimal(4));
                table.set_idCategory(_rs.getInt(5));
                clients.add(table);
            } while (_rs.next());
        } catch (Exception e) {
            System.err.println("Problem encountered getting all articles");
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public int update(Connection conn, int key, ArticleBean table) {
        int affected = -1;
        try {
            String sql = "update article set " +
                    "article_id = ?," +
                    "article_name = ?, " +
                    "article_availability = ?" +
                    "article_price = ?" +
                    "category_id = ?" +
                    "where client_id = ?";
            _pstm = conn.prepareStatement(sql);
            int i = 0;
            _pstm.setInt(++i, table.get_id());
            _pstm.setString(++i, table.get_name());
            _pstm.setInt(++i, table.get_availability());
            _pstm.setBigDecimal(++i, table.get_price());
            _pstm.setInt(++i, table.get_idCategory());
            _pstm.setInt(++i, key);
            affected = _pstm.executeUpdate();
        } catch (Exception e) {
            System.err.println("Problem encountered updating an article");
            e.printStackTrace();
        }
        return affected;
    }

    @Override
    public int delete(Connection conn, int key) {
        int affected = -1;
        try {
            String sql = "delete from article " +
                    "where article_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);
            affected = _pstm.executeUpdate();
        } catch (Exception e) {
            System.err.println("Problem encountered deleting an an article");
            e.printStackTrace();
        }
        return affected;
    }
}
