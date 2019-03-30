package dao.sql;

import java.sql.Connection;

public interface ConnectionSource {
    Connection getConnection() throws Exception;
}
