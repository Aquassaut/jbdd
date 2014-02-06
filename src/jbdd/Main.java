package jbdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    /*
    public static List<ClientBean> testClient(Connection conn, ClientManager um) throws Exception {
        ClientBean client1 = new ClientBean();
        ClientBean client2 = new ClientBean();
        ClientBean client3;
        int idc2;

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
    */

    public static void main(String[] args) {
        //A lancer avec -ea pour obtenir les AssertionError, afin que les tests aient un sens
		Connection conn = null;
        CategoryManager cm = new CategoryManager();
        ClientManager um = new ClientManager();
        BasketManager bm = new BasketManager();
        ArticleManager am = new ArticleManager();
        SaleManager om = new SaleManager();

		try {
			conn = Singleton.DS.getConnection();
            System.out.println("Connection OK");

            cm.createTable(conn);
            um.createTable(conn);
            bm.createTable(conn);
            am.createTable(conn);
            om.createTable(conn);

            System.out.println("Creation OK");

            List<CategoryBean> lcb = addCategories(cm, conn);
            List<ArticleBean> lab = addArticles(am, conn, lcb);
            displayArticlesTopLeastExpensive(am, conn);
            List<ClientBean> lub = createUsers(um, conn);
            conn.close();
            conn = Singleton.DS.getConnection();
            BasketBean bb1 = createBasketUser1(bm, conn, lub, lab);
            BasketBean bb2 = createBasketUser2(bm, conn, lub, lab);
            editBasketUser2(bm, conn, bb2, lab);
            /*
            displayBasketUser1();
            displayBasketUser2();
            validateBasketUser2();
            validateBasketUser1();
            displaySalesUser2();
            displayArticlesByBestSales();
              */
            /*
            System.out.println("Test client OK");

            List<ClientBean> clients = testClient(conn, um);
            System.out.println("Test client OK");
            */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ignore) {}
        }
    }

    public static List<CategoryBean> addCategories(CategoryManager cm, Connection conn) {
        //appareil photo, objectifs/zoom
        CategoryBean apPhoto = new CategoryBean();
        CategoryBean objZoom = new CategoryBean();
        apPhoto.set_name("Appareil Photo");
        objZoom.set_name("Objectifs/zoom");

        cm.create(conn, apPhoto);
        cm.create(conn, objZoom);
        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Categories fail !");
            e.printStackTrace();
        }
        List<CategoryBean> lcb = new ArrayList<CategoryBean>();
        lcb.add(apPhoto);
        lcb.add(objZoom);
        return lcb;
    }
    public static List<ArticleBean> addArticles(ArticleManager am, Connection conn, List<CategoryBean> lcb) {
        ArrayList<ArticleBean> aab = new ArrayList<ArticleBean>();
        ArticleBean refNum = new ArticleBean();
        ArticleBean kitRefNum = new ArticleBean();
        ArticleBean zoom = new ArticleBean();
        ArticleBean objectif = new ArticleBean();
        ArticleBean filtre = new ArticleBean();

        refNum.set_name("Reflex Numerique");
        refNum.set_availability(2);
        refNum.set_idCategory(lcb.get(0).get_id());
        refNum.set_price(new BigDecimal(1600));
        aab.add(refNum);

        kitRefNum.set_name("Kit Reflex Numerique");
        kitRefNum.set_availability(1);
        kitRefNum.set_idCategory(lcb.get(0).get_id());
        kitRefNum.set_price(new BigDecimal(530));
        aab.add(kitRefNum);

        zoom.set_name("zoom 24-105");
        zoom.set_availability(5);
        zoom.set_idCategory(lcb.get(1).get_id());
        zoom.set_price(new BigDecimal(748));
        aab.add(zoom);

        objectif.set_name("objectif 85mm");
        objectif.set_availability(1);
        objectif.set_idCategory(lcb.get(1).get_id());
        objectif.set_price(new BigDecimal(354.90));
        aab.add(objectif);

        filtre.set_name("filtre");
        filtre.set_availability(17);
        filtre.set_idCategory(lcb.get(1).get_id());
        filtre.set_price(new BigDecimal(17.80));
        aab.add(filtre);

        for (ArticleBean bean : aab) {
            am.create(conn, bean);
        }
        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Articles !");
            e.printStackTrace();
        }

        return aab;
    }
    public static void displayArticlesTopLeastExpensive(ArticleManager am, Connection conn) {
        for (ArticleBean bean : am.readAll(conn)) {
            System.out.println(bean.get_id() + " : " + bean.get_name() +
                    " (" + bean.get_price() + "$)");
        }
    }
    public static List<ClientBean> createUsers (ClientManager um, Connection conn) {
        List<ClientBean> lub = new ArrayList<ClientBean>();

        ClientBean user1 = new ClientBean();
        ClientBean user2 = new ClientBean();

        user1.set_name("User1");
        user1.set_password("12345");
        um.create(conn, user1);
        lub.add(user1);

        user2.set_name("User2");
        user2.set_password("54321");
        um.create(conn, user2);
        lub.add(user2);

        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Clients !");
            e.printStackTrace();
        }
        return lub;
    }
    public static BasketBean createBasketUser1(BasketManager bm,
            Connection conn, List<ClientBean> lub, List<ArticleBean> lab) throws Exception {

        BasketBean bb1 = new BasketBean();
        bb1.set_client(lub.get(0).get_id());

        List<ArticleBean> articles = new ArrayList<ArticleBean>();
        articles.add(lab.get(1));
        articles.add(lab.get(4));
        articles.add(lab.get(3));

        bb1.set_articles(articles);
        bm.create(conn, bb1);
        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Basket !");
            e.printStackTrace();
        }
        return bb1;
    }
    public static BasketBean createBasketUser2(BasketManager bm,
               Connection conn, List<ClientBean> lub, List<ArticleBean> lab) {
        BasketBean bb2 = new BasketBean();
        bb2.set_client(lub.get(1).get_id());

        List<ArticleBean> articles = new ArrayList<ArticleBean>();
        articles.add(lab.get(0));
        articles.add(lab.get(2));
        articles.add(lab.get(3));
        articles.add(lab.get(4));

        bb2.set_articles(articles);
        bm.create(conn, bb2);
        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Basket 2 !");
            e.printStackTrace();
        }
        return bb2;
    }

    public static void editBasketUser2(BasketManager bm, Connection conn, BasketBean bb2, List<ArticleBean> lab) {
        //pass
    }
}
