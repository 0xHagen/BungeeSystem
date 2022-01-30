package de.hanimehagen.bungeesystem.data;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.hanimehagen.bungeesystem.Configs;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

    public static DataSource initMySQLDataSource(Plugin plugin) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(Configs.getString(Configs.getMysql(), "host", "localhost"));
        dataSource.setPassword(Configs.getString(Configs.getMysql(), "password", ""));
        dataSource.setPortNumber(Integer.parseInt(Configs.getString(Configs.getMysql(), "port", "3306")));
        dataSource.setDatabaseName(Configs.getString(Configs.getMysql(), "database", "database"));
        dataSource.setUser(Configs.getString(Configs.getMysql(), "username", "root"));

        testDataSource(plugin, dataSource);

        return dataSource;
    }

    private static void testDataSource(Plugin plugin, DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("§cCould not establish database connection.");
            }
        }
        if (plugin != null) {
            plugin.getLogger().info("§aDatabase connection established.");
        }
    }

}
