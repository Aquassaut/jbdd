package jbdd;

import java.sql.Connection;
import java.util.List;

public class ArticleManager implements Queryable<ArticleBean> {

    public static final String TABLE_NAME = "article";
    public static final String CREATE_STMT = "" +
            "create table " + TABLE_NAME + " ( " +
            "id_" + TABLE_NAME +" serial " +
            "name_" + TABLE_NAME + " varchar(20)," +
            "availability_" + TABLE_NAME + " int(5), " +
            "price_" + TABLE_NAME + " float(5)," +
            "id_" + CategoryManager.TABLE_NAME + "_" + TABLE_NAME + " ) ;";

    public static void createTable(Connection conn) { }

	@Override
	public int create(Connection conn, ArticleBean table) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArticleBean read(Connection conn, int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleBean> readAll(Connection conn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Connection conn, int key, ArticleBean table) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Connection conn, int key) {
		// TODO Auto-generated method stub
		return 0;
	}
}
