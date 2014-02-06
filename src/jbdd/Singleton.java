package jbdd;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;


class Singleton {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://aquassaut.pwnz.org:10000/jbdd";
    private static final String USERNAME = "jbdd";
    private static final String PASSWORD = "jbdd";

    public static final DataSource DS = new BasicDataSource();
    static {
        BasicDataSource ds = (BasicDataSource) DS;
        ds.setDriverClassName(JDBC_DRIVER);
        ds.setUsername(USERNAME);
        ds.setPassword(PASSWORD);
        ds.setUrl(JDBC_URL);
        ds.setDefaultAutoCommit(false);
        ds.setDefaultTransactionIsolation(2); //truc
    }
}
