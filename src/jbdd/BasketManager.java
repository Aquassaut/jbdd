package jbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aquassaut
 * Date: 1/25/14
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasketManager implements Queryable<BasketBean>  {

    private PreparedStatement _pstm;

    @Override
    public int create(Connection conn, BasketBean table) {
        int res = -1;
        String lastStep = "Before connection";

        try {
            String sql = "insert into basket (id_user) values (?)";
            _pstm = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            _pstm.setInt(1, table.get_user().get_id());

            lastStep = "Executing update";

            res = _pstm.executeUpdate();

            if (0 >= res) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.err.println("Problem encountered inserting a basket with ID " + table.get_id());
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
    public BasketBean read(Connection conn, int key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<BasketBean> readAll(Connection conn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Connection conn, int key, BasketBean table) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int delete(Connection conn, int key) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
