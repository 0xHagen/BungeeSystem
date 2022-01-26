package de.hanimehagen.bungeesystem.mysql;

import de.hanimehagen.bungeesystem.Configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static final String host = Configs.getString(Configs.getMysql(), "host", "localhost");
    public static final String port = Configs.getString(Configs.getMysql(), "port", "3306");
    public static final String database = Configs.getString(Configs.getMysql(), "database", "database");
    public static final String username = Configs.getString(Configs.getMysql(), "username", "root");
    public static final String password = Configs.getString(Configs.getMysql(), "password", "");

    public static Connection connection;

    public static void connect() {
        try {
            if(connection == null || connection.isClosed()) {
                System.out.println("[MySQL] Connecting to database...");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                System.out.println("[MySQL] Connected succesfully!");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void disconnect() {
        if(connection != null) {
            try {
                connection.close();
                System.out.println("[MySQL] Disconnected succesfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void createTables() {
        PunishmentQuerys.createPunishmentTable();
        PlayerBaseQuerys.createPlayerTable();
    }

}
