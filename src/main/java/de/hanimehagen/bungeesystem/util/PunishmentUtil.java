package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.data.PunishmentData;
import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

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
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(punishMessagePath)).replace("%player%", name).replace("%operator%", operatorName).replace("%reason%", reason)));
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(alreadyMessagePath)).replace("%player%", name)));
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
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(unpunishMessagePath)).replace("%player%", name).replace("%operator%", operatorName)));
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(notPunishMessagePath)).replace("%player%", name)));
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
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(unbanIdMessagePath)).replace("%id%", id).replace("%operator%", operatorName)));
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.ERROR.replace("%class%", this.getClass().getName()))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString(cantFindIdMessagePath)).replace("%id%", id)));
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
