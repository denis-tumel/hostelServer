package dao.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class BaseConnectionPool implements ConnectionSource{

    private String driverName;
    private String user;
    private String password;
    private String url;
    private Connection connection;

    BaseConnectionPool(String driverName, String user, String password, String url) {
        this.driverName = driverName;
        this.user = user;
        this.password = password;
        this.url = url;
    }

    protected Connection connect() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
