package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.data.PunishmentData;
import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import javax.sql.DataSource;
import java.util.*;

public class PunishmentUtil {

    private final PunishmentData punishmentData;
    private final PlayerBaseData playerBaseData;

    private static final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public PunishmentUtil(Plugin plugin, DataSource dataSource) {
        this.punishmentData = new PunishmentData(plugin, dataSource);
        this.playerBaseData = new PlayerBaseData(plugin, dataSource);
    }

    public void punish(CommandSender sender, String name, String reasonId, PunishmentType type, HashMap<String, String> reasons, String alreadyMessagePath, String punishMessagePath) {
        Punishment punishment;
        String id = generateId();
        String uuid = this.playerBaseData.getUuidByName(name);
        String operatorUuid;
        String operatorName;
        if(!this.punishmentData.isPunished(uuid, type)) {
            if(sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                operatorUuid = player.getUniqueId().toString();
                operatorName = player.getName();
            } else {
                operatorUuid = "CONSOLE";
                operatorName = "CONSOLE";
            }
            String reason = reasons.get(reasonId).split("\\$")[0];
            long start = System.currentTimeMillis();
            long end;
            if(DurationUtil.getDuration(reasons.get(reasonId).split("\\$")[1]) == -1) {
                end = -1;
            } else {
                end = start + DurationUtil.getDuration(reasons.get(reasonId).split("\\$")[1]);
            }
            punishment = new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
            if(this.punishmentData.createPunishment(punishment)) {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(punishMessagePath)).replace("%player%", name).replace("%operator%", operatorName).replace("%reason%", reason)));
                if(type == PunishmentType.BAN && !Data.BANNED_PLAYERS.containsKey(uuid)) {
                    Data.BANNED_PLAYERS.put(uuid, end);
                } else if(type == PunishmentType.MUTE && !Data.MUTED_PLAYERS.containsKey(uuid)) {
                    Data.MUTED_PLAYERS.put(uuid, end);
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(alreadyMessagePath)).replace("%player%", name)));
        }
    }

    public void unpunishName(CommandSender sender, String name, PunishmentType type, String unpunishMessagePath, String notPunishMessagePath) {
        String operatorName;
        if(sender instanceof ProxiedPlayer) {
            operatorName = sender.getName();
        } else {
            operatorName = "CONSOLE";
        }
        String uuid = this.playerBaseData.getUuidByName(name);
        if(this.punishmentData.isPunished(uuid, type)) {
            if(this.punishmentData.deletePunishmentByUuid(uuid, type)) {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(unpunishMessagePath)).replace("%player%", name).replace("%operator%", operatorName)));
                if(type == PunishmentType.BAN && Data.BANNED_PLAYERS.containsKey(uuid)) {
                    Data.BANNED_PLAYERS.remove(uuid);
                } else if(type == PunishmentType.MUTE) {
                    Data.MUTED_PLAYERS.remove(uuid);
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(notPunishMessagePath)).replace("%player%", name)));
        }
    }

    public void unpunishId(CommandSender sender, String id, PunishmentType type, String unbanIdMessagePath, String cantFindIdMessagePath) {
        String operatorName;
        if(sender instanceof ProxiedPlayer) {
            operatorName = sender.getName();
        } else {
            operatorName = "CONSOLE";
        }
        if(this.punishmentData.existsId(id) && Objects.equals(this.punishmentData.getTypeById(id), type)) {
            if(this.punishmentData.deletePunishmentById(id, type)) {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(unbanIdMessagePath)).replace("%id%", id).replace("%operator%", operatorName)));
                String uuid = this.punishmentData.getUuidById(id, type);
                if(type == PunishmentType.BAN && Data.BANNED_PLAYERS.containsKey(uuid)) {
                    Data.BANNED_PLAYERS.remove(uuid);
                } else if(type == PunishmentType.MUTE) {
                    Data.MUTED_PLAYERS.remove(uuid);
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString(cantFindIdMessagePath)).replace("%id%", id)));
        }
    }

    public static void loadPunishReasons() {

        Configuration config = ConfigUtil.getConfig();

        Collection<String> muteReasonNodes = config.getSection("Reasons.Mute").getKeys();
        Collection<String> banReasonNodes = config.getSection("Reasons.Ban").getKeys();

        for(String key : muteReasonNodes) {
            String value = config.getString("Reasons.Mute." + key + ".Name") + "$" + config.getString("Reasons.Mute." + key + ".Duration");
            Data.MUTE_REASON_MAP.put(key, value);
        }

        for(String key : banReasonNodes) {
            String value = config.getString("Reasons.Ban." + key + ".Name") + "$" + config.getString("Reasons.Ban." + key + ".Duration");
            Data.BAN_REASON_MAP.put(key, value);
        }

    }

    private String generateId() {
        StringBuilder id = new StringBuilder();
        generateString(id);
        while (this.punishmentData.existsId(id.toString())) {
            generateString(id);
        }
        return id.toString();
    }

    private static void generateString(StringBuilder id) {
        for(int i = 0; i < 6; i++) {
            Random r = new Random();
            int low = 0;
            int high = 62;
            int result = r.nextInt(high-low) + low;
            id.append(chars[result]);
        }
    }

}
