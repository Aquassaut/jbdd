package jbdd;

import java.sql.Connection;
import java.util.List;

public interface Queryable<T> {
    public abstract int create(Connection conn, T table);
    public abstract T read(Connection conn, int key);
    public abstract List<T> readAll(Connection conn);
    public abstract int update(Connection conn, int key, T table);
    public abstract int delete(Connection conn, int key);
}
    
    /*{
        int n = -1;
        PreparedStatement pstm = null;
        try {
            String sql = "delete from ? where id_? = ?";
            pstm = conn.prepareStatement(sql);
            
            int i = 0;
            pstm.setString(++i, _name);
            pstm.setString(++i, _name);
            pstm.setInt(++i, key);

            if (1 != (n = pstm.executeUpdate())) {
                System.err.println("An error occured while deleting");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstm.close();
            } catch (Exception ignore) {};
        }
        return n;
    }
    */
