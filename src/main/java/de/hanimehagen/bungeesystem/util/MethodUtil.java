package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.command.*;
import de.hanimehagen.bungeesystem.data.DataSourceProvider;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.data.PunishmentData;
import de.hanimehagen.bungeesystem.listener.PostLoginListener;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodUtil {

    public static DataSource initDataSource(Plugin plugin) {
        try {
            return DataSourceProvider.initMySQLDataSource(plugin);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not establish database connection", e);
        }
        return null;
    }

    public static void initBootQuerys(Plugin plugin, DataSource dataSource) {
        PlayerBaseData playerBaseData = new PlayerBaseData(plugin, dataSource);
        PunishmentData punishmentData = new PunishmentData(plugin, dataSource);
        playerBaseData.createTable();
        punishmentData.createTable();
        if(punishmentData.deleteInvalidPunishments()) {
            plugin.getLogger().info("§aPunishments were successfully updated");
        }
        Data.BANNED_PLAYERS = punishmentData.getPunishedPlayers(PunishmentType.BAN);
        Data.MUTED_PLAYERS = punishmentData.getPunishedPlayers(PunishmentType.MUTE);
    }

    public static void initCommands(Plugin plugin, DataSource dataSource) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, new BanCommand("ban", "system.ban", plugin, dataSource));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new MuteCommand("mute", "system.mute", plugin, dataSource));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new CheckCommand("check", "system.check", plugin, dataSource));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new UnbanCommand("unban", "system.unban", plugin, dataSource));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new UnmuteCommand("unmute", "system.unmute", plugin, dataSource));
    }

    public static void initListeners(Plugin plugin, DataSource dataSource) {
        plugin.getProxy().getPluginManager().registerListener(plugin, new PostLoginListener(plugin, dataSource));
    }

    private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String format(String message) {
        Matcher match = pattern.matcher(message);
        while (match.find()) {
            String colour = message.substring(match.start(), match.end());
            message = message.replace(colour, ChatColor.of(colour) + "");
            match = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
