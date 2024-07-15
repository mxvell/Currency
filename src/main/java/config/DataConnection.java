package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class DataConnection {
    private static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlite:C:/Develop/IntelijProjects/Currency/src/main/resources/exchange.db");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        DATA_SOURCE = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
