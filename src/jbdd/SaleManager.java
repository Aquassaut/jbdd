package jbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaleManager implements Queryable<SaleBean>  {

    private PreparedStatement _pstm;
    private ResultSet _rs;
    public static String CREATE_STMT_MAIN = "" + 
                "create table sale ( " +
                "sale_id serial PRIMARY KEY, " +
                "sale_date date, " +
                "sale_price real, " + 
                "client_id int references client(client_id) )";

    public static String CREATE_STMT_SEC = "" + 
                "create table purchased ( " +
                "sale_id int references sale(sale_id), " +
                "article_id int references article(article_id), " +
                "quantity int )";


    public boolean createTable(Connection conn) {
        boolean success;
        String lastStep = "Before connection";
        try {
            String sql = "drop table if exists purchased cascade";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table purchased";
            _pstm.execute();

            sql = "drop table if exists sale";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table sale";
            _pstm.execute();


            _pstm = conn.prepareStatement(CREATE_STMT_MAIN);
            lastStep = "recreating table sale";
            success = _pstm.execute();

            _pstm = conn.prepareStatement(CREATE_STMT_SEC);
            lastStep = "recreating table purchased";
            success = success && _pstm.execute();

        } catch (Exception e) {
            System.err.println("Problem encountered creating sale tables");
            System.err.println(lastStep);
            e.printStackTrace();
            success = false;
        }
        return success;
    }


    @Override
    public int create(Connection conn, SaleBean table) {
        int res = -1;
        String lastStep = "Before connection";

        try {
            String sql = "insert into sale (sale_date, sale_price, client_id) values (?, ?, ?)";
            _pstm = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            _pstm.setDate(1, table.get_date());
            _pstm.setBigDecimal(2, table.get_price());
            _pstm.setInt(3, table.get_client());
            lastStep = "Executing update";
            res = _pstm.executeUpdate();

            ArrayList<ArticleBean> temp = new ArrayList<ArticleBean>();
            Iterator<ArticleBean> it = table.get_articles().iterator();
            while(it.hasNext()) {
                temp.add(it.next());
            }

            sql = "delete * from purchased where sale_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, table.get_id());
            _pstm.executeUpdate();

            while (0 != temp.size()) {
                int index = -1;
                int quantity = 0;
                ArticleBean ab = temp.get(0);
                while (temp.contains(ab)) {
                    quantity += 1;
                    temp.remove(temp.lastIndexOf(ab));
                }
                sql = "insert into purchased(sale_id, article_id, quantity) "+
                    "values (?, ?, ?)";
                _pstm = conn.prepareStatement(sql);
                _pstm.setInt(1, table.get_id());
                _pstm.setInt(2, ab.get_id());
                _pstm.setInt(3, quantity);
                _pstm.executeUpdate();
            }

            if (0 >= res) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.err.println("Problem encountered inserting a sale with ID " + table.get_id());
            System.err.println(lastStep);
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {}
        }
        return res;
    }

    @Override
    public SaleBean read(Connection conn, int key) {
        SaleBean table = new SaleBean();
        try {
            String sql = "select sale_id, sale_date, sale_price, client_id" +
                        "from sale " +
                        "where sale_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            _rs = _pstm.executeQuery();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to read sale number " + key);
            }
            table.set_id(_rs.getInt(1));
            table.set_date(_rs.getDate(2));
            table.set_price(_rs.getBigDecimal(3));
            table.set_client(_rs.getInt(4));

            sql = "select article_id, quantity from purchased where sale_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);
            _rs = _pstm.executeQuery();

            ArrayList<ArrayList<Integer>> articles = new ArrayList<ArrayList<Integer>>();
            while (_rs.next()) {
                ArrayList<Integer> art = new ArrayList<Integer>();
                art.add(_rs.getInt(1));
                art.add(_rs.getInt(2));
                articles.add(art);
            }

            List<ArticleBean> lab = new ArrayList<ArticleBean>();
            ArticleManager am = new ArticleManager();

            for (ArrayList<Integer> articlePair : articles) {
                ArticleBean ab = am.read(conn, articlePair.get(0));
                for (int i = 0; i < articlePair.get(1); i += 1) {
                    lab.add(ab);
                }
            }
        } catch (Exception e) {
            System.err.println("Problem encountered reading a sale");
            e.printStackTrace();
        }
        return table;

    }

    @Override
    public List<SaleBean> readAll(Connection conn) {
        List<SaleBean> lbb = new ArrayList<SaleBean>();
        try {
            String sql = "select sale_id " +
                        "from sale";
            _pstm = conn.prepareStatement(sql);
            _rs = _pstm.executeQuery();
            while (_rs.next()) {
                lbb.add(this.read(conn, _rs.getInt(1)));
            }
        } catch (Exception e) {
            System.err.println("Problem encountered reading all sales");
            e.printStackTrace();
        }
        return lbb;
    }

    @Override
    public int update(Connection conn, int key, SaleBean table) {
        // WOOHOO dégueulasse.
        // TODO: refaire ça : on a la mauvaise valeur de retour et
        // on fait trop d'appels sql
        this.delete(conn, key);
        return this.create(conn, table);
    }

    @Override
    public int delete(Connection conn, int key) {
        int affected = -1;
        try {
            String sql = "delete from sale " +
                         "where sale_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);
            affected = _pstm.executeUpdate();

            sql = "delete from purchased " +
                         "where sale_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            affected += _pstm.executeUpdate();

        } catch (Exception e) {
            System.err.println("Problem encountered deleting a sale");
            e.printStackTrace();
        }
        return affected;
    }
}
