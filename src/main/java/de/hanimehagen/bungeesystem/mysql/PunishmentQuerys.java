package de.hanimehagen.bungeesystem.mysql;

import de.hanimehagen.bungeesystem.punishment.Punishment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PunishmentQuerys {

    public static void createPunishmentTable() {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Punishments ("+
                            "id VARCHAR(8) NULL DEFAULT NULL," +
                            "name VARCHAR(16) NULL DEFAULT NULL," +
                            "uuid VARCHAR(36) NULL DEFAULT NULL," +
                            "reason VARCHAR(255) NULL DEFAULT NULL," +
                            "operator VARCHAR(16) NULL DEFAULT NULL," +
                            "type VARCHAR(16) NULL DEFAULT NULL," +
                            "start LONG DEFAULT NULL," +
                            "end LONG DEFAULT NULL," +
                            "PRIMARY KEY (`id`))");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPunishment(Punishment punishment) {

    }

    public static boolean existsId(String id) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Punishments WHERE id = ?");
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

}