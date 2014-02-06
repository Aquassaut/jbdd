package jbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BasketManager implements Queryable<BasketBean>  {

    private PreparedStatement _pstm;
    private ResultSet _rs;
    private static final String CREATE_STMT_MAIN = "" +
                "create table basket ( " +
                "basket_id serial PRIMARY KEY, " +
                "client_id int references client(client_id) )";

    private static final String CREATE_STMT_SEC = "" +
                "create table contains ( " +
                "basket_id int references basket(basket_id), " +
                "article_id int references article(article_id), " +
                "quantity int)";


    public boolean createTable(Connection conn) {
        String lastStep = "Before connection";
        try {
            String sql = "drop table if exists contains cascade";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table contains";
            _pstm.execute();

            sql = "drop table if exists basket cascade";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table basket";
            _pstm.execute();


            _pstm = conn.prepareStatement(CREATE_STMT_MAIN);
            lastStep = "recreating table basket";
            _pstm.execute();

            _pstm = conn.prepareStatement(CREATE_STMT_SEC);
            lastStep = "recreating table contains";
            _pstm.execute();

        } catch (Exception e) {
            System.err.println("Problem encountered creating basket tables");
            System.err.println(lastStep);
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public int create(Connection conn, BasketBean table) {
        int res = -1;
        String lastStep = "Before connection";

        try {
            String sql = "insert into basket (client_id) values (?)";
            _pstm = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            _pstm.setInt(1, table.get_client());
            lastStep = "Executing update";
            _pstm.executeUpdate();
            _rs = _pstm.getGeneratedKeys();

            if (! _rs.next()) {
                throw new Exception("pas de clé pour le basket");
            }
            res = _rs.getInt(1);
            table.set_id(res);

            ArrayList<ArticleBean> temp = new ArrayList<ArticleBean>();
            for (ArticleBean ab : table.get_articles()) {
                temp.add(ab);
            }

            sql = "delete from contains where basket_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, table.get_id());
            _pstm.executeUpdate();

            while (0 != temp.size()) {
                int quantity = 0;
                ArticleBean ab = temp.get(0);
                while (temp.contains(ab)) {
                    quantity += 1;
                    temp.remove(temp.lastIndexOf(ab));
                }
                sql = "insert into contains (basket_id, article_id, quantity) " +
                    "values (?, ?, ?)";
                _pstm = conn.prepareStatement(sql);
                int i = 0;
                _pstm.setInt(++i, table.get_id());
                _pstm.setInt(++i, ab.get_id());
                _pstm.setInt(++i, quantity);
                _pstm.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Problem encountered inserting a basket with ID " + table.get_id());
            System.err.println(lastStep);
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public BasketBean read(Connection conn, int key) {
        BasketBean table = new BasketBean();
        try {
            String sql = "select basket_id, client_id " +
                        "from basket " +
                        "where basket_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            _rs = _pstm.executeQuery();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to read basket number " + key);
            }
            table.set_id(_rs.getInt(1));
            table.set_client(_rs.getInt(2));

            sql = "select article_id, quantity from contains where basket_id = ?";
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
            table.set_articles(lab);
        } catch (Exception e) {
            System.err.println("Problem encountered reading a basket");
            e.printStackTrace();
        }
        return table;

    }

    @Override
    public List<BasketBean> readAll(Connection conn) {
        List<BasketBean> lbb = new ArrayList<BasketBean>();
        ResultSet outerRs;
        try {
            String sql = "select basket_id from basket";
            _pstm = conn.prepareStatement(sql);
            outerRs = _pstm.executeQuery();
            while (outerRs.next()) {
                lbb.add(this.read(conn, outerRs.getInt(1)));
            }
        } catch (Exception e) {
            System.err.println("Problem encountered reading all baskets");
            e.printStackTrace();
        }
        return lbb;
    }

    @Override
    public int update(Connection conn, int key, BasketBean table) {
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
            String sql = "delete from contains " +
                    "where basket_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);
            affected = _pstm.executeUpdate();

            sql = "delete from basket " +
                         "where basket_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);


            affected += _pstm.executeUpdate();

        } catch (Exception e) {
            System.err.println("Problem encountered deleting a basket");
            e.printStackTrace();
        }
        return affected;
    }
}
