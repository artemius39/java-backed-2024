package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;

public class IntegrationTest extends IntegrationEnvironment {
    @Test
    void migrationsAreRun() throws SQLException {
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );

        Statement statement = connection.createStatement();

        statement.execute("select * from \"user\""); // must not throw
        statement.execute("select * from link"); // must not throw
        statement.execute("select * from user_link"); // must not throw
    }
}
