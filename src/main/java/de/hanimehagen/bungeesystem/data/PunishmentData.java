package de.hanimehagen.bungeesystem.data;

import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PunishmentData extends PluginDataHolder {

    //TODO: Optimize exist querys

    public PunishmentData(Plugin plugin, DataSource source) {
        super(plugin, source);
    }

    public void createTable() {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
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
                        "PRIMARY KEY (`id`))")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            logSQLError("Could not create PunishmentTable", e);
        }
    }

    public boolean createPunishment(Punishment punishment) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Punishments (id, name, uuid, reason, operator, operatorUuid, type, start, end) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, punishment.getId());
            stmt.setString(2, punishment.getName());
            stmt.setString(3, punishment.getUuid());
            stmt.setString(4, punishment.getReason());
            stmt.setString(5, punishment.getOperatorName());
            stmt.setString(6, punishment.getOperatorUuid());
            stmt.setString(7, punishment.getType().toString());
            stmt.setLong(8, punishment.getStartTime());
            stmt.setLong(9, punishment.getEndTime());
            int updated = stmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            logSQLError("Could not create Punishment", e);
        }
        return false;
    }

    public boolean existsId(String id) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Punishments WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logSQLError("Could not check if ID exists", e);
        }
        return false;
    }

    public Punishment getPunishmentById(String id) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Punishments WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                String operatorUuid = resultSet.getString("operatorUuid");
                String operatorName = resultSet.getString("operator");
                String reason = resultSet.getString("reason");
                PunishmentType type = PunishmentType.valueOf(resultSet.getString("type"));
                long start = resultSet.getLong("start");
                long end = resultSet.getLong("end");
                return new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
            }
        } catch (SQLException e) {
            logSQLError("Could not get Punishment", e);
        }
        return null;
    }

    public Punishment getPunishmentByType(String uuid, PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Punishments WHERE uuid = ? AND type = ?")) {
            stmt.setString(1, uuid);
            stmt.setString(2, type.toString());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String operatorUuid = resultSet.getString("operatorUuid");
                String operatorName = resultSet.getString("operator");
                String reason = resultSet.getString("reason");
                long start = resultSet.getLong("start");
                long end = resultSet.getLong("end");
                return new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
            }
        } catch (SQLException e) {
            logSQLError("Could not get Punishment by type", e);
        }
        return null;
    }

    public boolean isPunished(String uuid, PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM Punishments WHERE uuid = ? AND type = ? AND (end > ? OR end = -1)")) {
            stmt.setString(1, uuid);
            stmt.setString(2, type.toString());
            stmt.setLong(3, System.currentTimeMillis());
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logSQLError("Could not check if player is punished", e);
        }
        return false;
    }

    public PunishmentType getTypeById(String id) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT type FROM Punishments WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return PunishmentType.valueOf(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            logSQLError("Could not get PunishmentType", e);
        }
        return null;
    }

    public String getUuidById(String id, PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid FROM Punishments WHERE id = ? AND type = ?")) {
            stmt.setString(1, id);
            stmt.setString(2, type.toString());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString("uuid");
            }
        } catch (SQLException e) {
            logSQLError("Could not get UUID", e);
        }
        return null;
    }

    public boolean deletePunishmentById(String id, PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Punishments WHERE id = ? AND type = ?")) {
            stmt.setString(1, id);
            stmt.setString(2, type.toString());
            int updated = stmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            logSQLError("Could not delete Punishment", e);
        }
        return false;
    }

    public boolean deletePunishmentByUuid(String uuid, PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Punishments WHERE uuid = ? AND type = ?")) {
            stmt.setString(1, uuid);
            stmt.setString(2, type.toString());
            int updated = stmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            logSQLError("Could not delete Punishment", e);
        }
        return false;
    }

    public HashMap<String, Long> getPunishedPlayers(PunishmentType type) {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT uuid, end FROM Punishments WHERE type = ?")) {
            stmt.setString(1, type.toString());
            ResultSet resultSet = stmt.executeQuery();
            HashMap<String, Long> punished = new HashMap<>();
            while (resultSet.next()) {
                punished.put(resultSet.getString("uuid"), resultSet.getLong("end"));
            }
            return punished;
        } catch (SQLException e) {
            logSQLError("Could not get Punishment Players", e);
        }
        return null;
    }

    public boolean deleteInvalidPunishments() {
        try (Connection conn = conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Punishments WHERE end < ? AND end != -1")) {
            stmt.setLong(1, System.currentTimeMillis());
            int updated = stmt.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            logSQLError("Could not delete invalid Punishment", e);
        }
        return false;
    }

}
