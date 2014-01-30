package jbdd;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static List<ClientBean> testClient(Connection conn) throws Exception {
        ClientManager um = new ClientManager();
        ClientBean client1 = new ClientBean();
        ClientBean client2 = new ClientBean();
        ClientBean client3;
        int idc2;


        um.createTable(conn);

        client1.set_name("meg asin");
        client1.set_password("1234");
        client1.set_id(8);
        um.create(conn, client1);
        assert 1 == client1.get_id() :
                "L'id client aurait du être modifié pour refleter la vraie valeur";

        client2.set_name("Gerald labranche");
        client2.set_password("azerty");
        idc2 = um.create(conn, client2);
        assert 2 == idc2:"La méthode devrait retourner l'id après insertion";

        client3 = um.read(conn, client1.get_id());
        assert client1.get_id() == client3.get_id() &&
                client1.get_name().equals(client3.get_name()) &&
                client1.get_password().equals(client3.get_password()) :
                "Les clients 1 et 3 devrait être identiques";
        return Arrays.asList(client1, client2);
    }

    public static void main(String[] args) {
        //A lancer avec -ea pour obtenir les AssertionError, afin que les tests aient un sens
		Connection conn = null;
		try {
			conn = Singleton.DS.getConnection();
            System.out.println("Connection OK");

            List<ClientBean> clients = testClient(conn);
            System.out.println("Test client OK");


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception ignore) {}
		}
	}

}
