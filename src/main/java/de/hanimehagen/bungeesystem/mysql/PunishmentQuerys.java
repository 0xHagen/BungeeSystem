package de.hanimehagen.bungeesystem.mysql;

import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;

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
                            "operatorUuid VARCHAR(36) NULL DEFAULT NULL," +
                            "type VARCHAR(8) NULL DEFAULT NULL," +
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
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO Punishments (id, name, uuid, reason, operator, operatorUuid, type, start, end) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, punishment.getId());
            ps.setString(2, punishment.getName());
            ps.setString(3, punishment.getUuid());
            ps.setString(4, punishment.getReason());
            ps.setString(5, punishment.getOperatorName());
            ps.setString(6, punishment.getOperatorUuid());
            ps.setString(7, punishment.getType().toString());
            ps.setLong(8, punishment.getStartTime());
            ps.setLong(9, punishment.getEndTime());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static boolean isPunishedByUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT end FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            long end = 0;
            while (rs.next()) {
                end = rs.getLong("end");
            }
            ps.close();
            rs.close();
            long now = System.currentTimeMillis() / 1000;
            return end > now || end == -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getReasonByPunishedUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT reason FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            String reason = null;
            while (rs.next()) {
                reason = rs.getString("reason");
            }
            ps.close();
            rs.close();
            return reason;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOperatorUuidByPunishedUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT operatorUuid FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            String uuid = null;
            while (rs.next()) {
                uuid = rs.getString("operatorUuid");
            }
            ps.close();
            rs.close();
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOperatorByPunishedUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT operator FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            String name = null;
            while (rs.next()) {
                name = rs.getString("operatorUuid");
            }
            ps.close();
            rs.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getStartTimeByUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT start FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            long start = 0;
            while (rs.next()) {
                start = rs.getLong("start");
            }
            ps.close();
            rs.close();
            return start;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getEndTimeByUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT end FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            long end = 0;
            while (rs.next()) {
                end = rs.getLong("end");
            }
            ps.close();
            rs.close();
            return end;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}