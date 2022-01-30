package de.hanimehagen.bungeesystem.data;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerBaseData extends PluginDataHolder {

    public PlayerBaseData(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public void createTable() {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS PlayerBase ("+
                        "uuid VARCHAR(36) NULL DEFAULT NULL," +
                        "name VARCHAR(16) NULL DEFAULT NULL," +
                        "PRIMARY KEY (uuid))")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSQLError("Could not create PlayerBaseTable", e);
        }
    }

    public boolean existsName(String name) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM PlayerBase WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logSQLError("Could not check if Name exists", e);
        }
        return false;
    }

    public boolean existsUuid(String uuid) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM PlayerBase WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logSQLError("Could not check if Uuid exists", e);
        }
        return false;
    }

    public void createPlayer(ProxiedPlayer player) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO PlayerBase (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?")) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSQLError("Could not create Player", e);
        }
    }

    public String getNameByUuid(String uuid) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT name FROM PlayerBase WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            logSQLError("Could not get PlayerName", e);
        }
        return null;
    }

    public String getUuidByName(String name) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid FROM PlayerBase WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("uuid");
            }
        } catch (SQLException e) {
            logSQLError("Could not get PlayerUuid", e);
        }
        return null;
    }

    public void deleteNotValidPlayer(ProxiedPlayer player) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM PlayerBase WHERE name = ? AND uuid != ?")) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getUniqueId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSQLError("Could not delete Player", e);
        }
    }

}
