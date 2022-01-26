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
                            "id VARCHAR(6) NULL DEFAULT NULL," +
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

    public static Punishment getPunishmentbyId(String id) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Punishments WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            String uuid = null;
            String name = null;
            String operatorUuid = null;
            String operatorName = null;
            String reason = null;
            PunishmentType type = null;
            long start = 0;
            long end = 0;
            while (rs.next()) {
                uuid = rs.getString("uuid");
                name = rs.getString("name");
                operatorUuid = rs.getString("operatorUuid");
                operatorName = rs.getString("operator");
                reason = rs.getString("reason");
                type = PunishmentType.valueOf(rs.getString("type"));
                start = rs.getLong("start");
                end = rs.getLong("end");
            }
            return new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Punishment getPunishmentbyUuidAndType(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ResultSet rs = ps.executeQuery();
            String uuid = null;
            String name = null;
            String operatorUuid = null;
            String operatorName = null;
            String reason = null;
            long start = 0;
            long end = 0;
            while (rs.next()) {
                uuid = rs.getString("uuid");
                name = rs.getString("name");
                operatorUuid = rs.getString("operatorUuid");
                operatorName = rs.getString("operator");
                reason = rs.getString("reason");
                start = rs.getLong("start");
                end = rs.getLong("end");
            }
            return new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public static PunishmentType getTypeByPunishId(String id) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT type FROM Punishments WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            String type = null;
            while (rs.next()) {
                type = rs.getString("type");
            }
            ps.close();
            rs.close();
            assert type != null;
            if(type.equals(PunishmentType.BAN.toString())) {
                return PunishmentType.BAN;
            } else if(type.equals(PunishmentType.MUTE.toString())) {
                return PunishmentType.MUTE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deletePunishmentByUuid(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Punishments WHERE uuid = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePunishmentByPunishId(String id, PunishmentType type) {
        try {
            MySQL.connect();
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM Punishments WHERE id = ? AND type = ?");
            ps.setString(1, id);
            ps.setString(2, type.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}