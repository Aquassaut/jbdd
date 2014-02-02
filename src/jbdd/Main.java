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
        assert client1.equals(client3) : "Les clients 1 et 3 devrait être identiques";

        List<ClientBean> allClients = um.readAll(conn);
        assert allClients.get(0).equals(client1) && allClients.get(1).equals(client2) :
                "Les deux premiers clients devraient être ceux que l'on vient de créer";


        client3.set_name("Hosni");
        um.update(conn, client1.get_id(), client3);
        assert ! client1.equals(um.read(conn, client1.get_id())):
                "Le client 1 devrait avoir changé";



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
