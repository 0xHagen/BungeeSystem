package de.hanimehagen.bungeesystem.mysql;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerQuerys {

    public static void createPlayerTable() {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Player ("+
                            "uuid VARCHAR(36) NULL DEFAULT NULL," +
                            "name VARCHAR(16) NULL DEFAULT NULL," +
                            "PRIMARY KEY (uuid))");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean existsId(String id) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Player WHERE uuid = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            ps.close();
            rs.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean existsName(String name) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Player WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            ps.close();
            rs.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createPlayer(ProxiedPlayer player) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Player (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setString(3, player.getName());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
