package fyodor.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectorDB {
    public static Connection getConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("application");
        String url = resource.getString("spring.datasource.url");
        String user = resource.getString("spring.datasource.username");
        String pass = resource.getString("spring.datasource.password");
        return DriverManager.getConnection(url, user, pass);
    }
}
