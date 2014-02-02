package jbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientManager implements Queryable<ClientBean> {
    private PreparedStatement _pstm;
    private ResultSet _rs;

    public boolean createTable(Connection conn) {
        boolean success;
        String lastStep = "Before connection";
        try {
            String sql = "drop table if exists client";
            _pstm = conn.prepareStatement(sql);
            lastStep = "dropping table";
            _pstm.execute();

            sql = "create table client (" +
                    "client_id serial," +
                    "client_name varchar(20)," +
                    "client_password varchar(20)" +
                  ")";
            _pstm = conn.prepareStatement(sql);
            lastStep = "recreating table";
            success = _pstm.execute();
        } catch (Exception e) {
            System.err.println("Problem encountered creating client table");
            System.err.println(lastStep);
            e.printStackTrace();
            success = false;
        }
        return success;
    }

	@Override
	public int create(Connection conn, ClientBean table) {
        int res = -1;
        try {
            String sql = "insert into client (client_name, client_password) values (?, ?)";
            _pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            _pstm.setString(1, table.get_name());
            _pstm.setString(2, table.get_password());


            _pstm.executeUpdate();
            _rs = _pstm.getGeneratedKeys();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to insert new client in the database");
            }
            res = _rs.getInt(1); //la premiere colonne est l'id
            table.set_id(res);
        } catch (Exception e) {
            System.err.println("Problem encountered inserting a client");
            e.printStackTrace();
        }
        return res;
	}

	@Override
	public ClientBean read(Connection conn, int key) {
        ClientBean table = new ClientBean();
        try {
            String sql = "select client_id, client_name, client_password " +
                    "from client " +
                    "where client_id = ?";
            _pstm = conn.prepareStatement(sql);
            _pstm.setInt(1, key);

            _rs = _pstm.executeQuery();
            if (! _rs.next()) {
                throw new RuntimeException("Failed to read client number " + key);
            }
            table.set_id(_rs.getInt(1));
            table.set_name(_rs.getString(2));
            table.set_password(_rs.getString(3));
        } catch (Exception e) {
            System.err.println("Problem encountered reading a client");
            e.printStackTrace();
        }
		return table;
	}

	@Override
	public List<ClientBean> readAll(Connection conn) {
        ArrayList<ClientBean> clients = null;
        try {
            String sql = "select client_id, client_name, client_password " +
                    "from client ";
            _pstm = conn.prepareStatement(sql);
            _rs = _pstm.executeQuery();

            if (! _rs.next()) {
                throw new RuntimeException("Failed to read all clients");
            }
            clients = new ArrayList<ClientBean>();
            do {
                ClientBean table = new ClientBean();
                table.set_id(_rs.getInt(1));
                table.set_name(_rs.getString(2));
                table.set_password(_rs.getString(3));
                clients.add(table);
            } while (_rs.next());
        } catch (Exception e) {
            System.err.println("Problem encountered getting all client");
            e.printStackTrace();
        }
		return clients;
	}

	@Override
	public int update(Connection conn, int key, ClientBean table) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Connection conn, int key) {
		// TODO Auto-generated method stub
		return 0;
	}

}
