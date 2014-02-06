package jbdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;

public class Main {

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
            displayBasket(bm, conn, lub.get(0));
            displayBasket(bm, conn, lub.get(1));
            validateBasket(om, conn, bb2);
            validateBasket(om, conn, bb1);
            displaySalesUser2(om, conn,lub.get(1));
            displayArticlesByBestSales(om, conn);

            System.out.println("Fin de l'exercice");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignore) {}
        }
    }

    private static List<CategoryBean> addCategories(CategoryManager cm, Connection conn) {
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
    private static List<ArticleBean> addArticles(ArticleManager am, Connection conn, List<CategoryBean> lcb) {
        ArrayList<ArticleBean> aab = new ArrayList<ArticleBean>();
        ArticleBean refNum = new ArticleBean();
        ArticleBean kitRefNum = new ArticleBean();
        ArticleBean zoom = new ArticleBean();
        ArticleBean objectif = new ArticleBean();
        ArticleBean filtre = new ArticleBean();

        refNum.set_name("Reflex Numerique");
        refNum.set_availability(2);
        refNum.set_idCategory(lcb.get(0).get_id());
        refNum.set_price(new BigDecimal("1600"));
        aab.add(refNum);

        kitRefNum.set_name("Kit Reflex Numerique");
        kitRefNum.set_availability(1);
        kitRefNum.set_idCategory(lcb.get(0).get_id());
        kitRefNum.set_price(new BigDecimal("530"));
        aab.add(kitRefNum);

        zoom.set_name("zoom 24-105");
        zoom.set_availability(5);
        zoom.set_idCategory(lcb.get(1).get_id());
        zoom.set_price(new BigDecimal("748"));
        aab.add(zoom);

        objectif.set_name("objectif 85mm");
        objectif.set_availability(1);
        objectif.set_idCategory(lcb.get(1).get_id());
        objectif.set_price(new BigDecimal("354.9"));
        aab.add(objectif);

        filtre.set_name("filtre");
        filtre.set_availability(17);
        filtre.set_idCategory(lcb.get(1).get_id());
        filtre.set_price(new BigDecimal("17.8"));
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
    private static void displayArticlesTopLeastExpensive(ArticleManager am, Connection conn) {
        for (ArticleBean bean : am.readAll(conn)) {
            System.out.println(bean.get_id() + " : " + bean.get_name() +
                    " (" + bean.get_price() + "$)");
        }
    }
    private static List<ClientBean> createUsers (ClientManager um, Connection conn) {
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
    private static BasketBean createBasketUser1(BasketManager bm,
            Connection conn, List<ClientBean> lub, List<ArticleBean> lab) {

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
    private static BasketBean createBasketUser2(BasketManager bm,
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

    private static void editBasketUser2(BasketManager bm, Connection conn, BasketBean bb2, List<ArticleBean> lab) {
        List<ArticleBean> articles = bb2.get_articles();
        articles.remove(lab.get(4));
        articles.add(lab.get(0));
        bb2.set_articles(articles);
        bm.update(conn, bb2.get_id(), bb2);

        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Basket 2 !");
            e.printStackTrace();
        }
    }
    private static void displayBasket(BasketManager bm, Connection conn, ClientBean user) {
        List<BasketBean> baskets = bm.readAll(conn);
        for (BasketBean bb : baskets) {
            if (bb.get_client() == user.get_id()) {
                System.out.println("Panier numéro " + bb.get_id() + " du client " + user.get_id());
                for (ArticleBean ab : bb.get_articles()) {
                    System.out.println(ab.get_id() + " : " + ab.get_name() + " (" + ab.get_price() + "$)");
                }
            }
        }
    }
    private static SaleBean validateBasket(SaleManager om, Connection conn, BasketBean bb) {
        SaleBean sb = new SaleBean();
        sb.set_articles(bb.get_articles());
        sb.set_client(bb.get_client());
        sb.set_date(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
        BigDecimal price = new BigDecimal(0);
        for (ArticleBean a : bb.get_articles()) {
            price = price.add(a.get_price());
        }
        sb.set_price(price);

        om.create(conn, sb);
        try {
            conn.commit();
        } catch (Exception e) {
            System.err.println("Sase user " + bb.get_client() + " !");
            e.printStackTrace();
        }
        return sb;
    }
    private static void displaySalesUser2(SaleManager om, Connection conn, ClientBean ub) {
        List<SaleBean> lsb = om.readAll(conn);
        for (SaleBean sb : lsb) {
            if (sb.get_client() == ub.get_id()) {
                System.out.println("Vente n° " + sb.get_id() + " du client " + ub.get_id() + " pour " + sb.get_price() +
                "$ le " + sb.get_date());
                for (ArticleBean ab : sb.get_articles()) {
                    System.out.println(ab.get_id() + " : " + ab.get_name() + " (" + ab.get_price() + "$)");
                }
            }
        }
    }

    private static void displayArticlesByBestSales(SaleManager om, Connection conn) {
        List<SaleBean> lsb = om.readAll(conn);
        final HashMap<Integer, Integer> idsAndQuantity = new HashMap<Integer, Integer>();
        List<ArticleBean> allArticles = new ArrayList<ArticleBean>();
        for (SaleBean sb : lsb) {
            for (ArticleBean ab : sb.get_articles()) {
                if (! idsAndQuantity.containsKey(ab.get_id())) {
                    idsAndQuantity.put(ab.get_id(), 1);
                    allArticles.add(ab);
                } else {
                    idsAndQuantity.put(ab.get_id(), idsAndQuantity.get(ab.get_id()) + 1);
                }
            }
        }
        Collections.sort(allArticles, new Comparator<ArticleBean>() {
            @Override
            public int compare(ArticleBean a1, ArticleBean a2) {
                return idsAndQuantity.get(a2.get_id()).compareTo(idsAndQuantity.get(a1.get_id()));
            }
        });

        System.out.println("Les meilleures ventes : ");
        for (ArticleBean ab : allArticles) {
            System.out.println(idsAndQuantity.get(ab.get_id()) + " vendus : " + ab.get_id() + " : " + ab.get_name() + " (" + ab.get_price() + "$)");
        }
    }

}
