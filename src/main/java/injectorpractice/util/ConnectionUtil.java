package injectorpractice.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static final String USER = "root";
    private static final String PASSWORD = "1979";

    public static Connection getConnection() {
        Properties properties = new Properties();
        properties.put("user", USER);
        properties.put("password", PASSWORD);
        String url = "jdbc:mysql://localhost:3306/taxi?serverTimezone=UTC";
        try {
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new RuntimeException("Can't connect to database", e);
        }
    }
}
