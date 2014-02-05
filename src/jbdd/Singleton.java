package jbdd;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;


public class Singleton {

    public static final String JDBC_DRIVER = "org.postgresql.Driver";
    public static final String JDBC_URL = "jdbc:postgresql://aquassaut.pwnz.org:10000/jbdd";
    public static final String USERNAME = "jbdd";
    public static final String PASSWORD = "jbdd";

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
