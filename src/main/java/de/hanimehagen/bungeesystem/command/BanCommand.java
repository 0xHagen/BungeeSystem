package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerQuerys;
import de.hanimehagen.bungeesystem.mysql.PunishmentQuerys;
import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.DurationUtil;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

    public BanCommand(String name, String permission) {
        super("ban", "system.ban");
    }

    //TODO: Check if player is banned
    //TODO: Add perma ban support

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.ban")) {
            if (args.length == 2) {
                if (PlayerQuerys.existsName(args[0])) {
                    if (Data.BAN_REASON_MAP.containsKey(args[1])) {
                        Punishment punishment;
                        String id = PunishmentUtil.generateId();
                        String uuid = PlayerQuerys.getUuid(args[0]);
                        String name = args[0];
                        String operatorUuid;
                        String operatorName;
                        if (sender instanceof ProxiedPlayer) {
                            ProxiedPlayer player = (ProxiedPlayer) sender;
                            operatorUuid = player.getUniqueId().toString();
                            operatorName = player.getName();
                        } else {
                            operatorUuid = "CONSOLE";
                            operatorName = "CONSOLE";
                        }
                        String reason = Data.BAN_REASON_MAP.get(args[1]).split("\\$")[0];
                        PunishmentType type = PunishmentType.BAN;
                        long start = System.currentTimeMillis();
                        long end = start + DurationUtil.getDuration(Data.BAN_REASON_MAP.get(args[1]).split("\\$")[1]);
                        punishment = new Punishment(id, uuid, name, operatorUuid, operatorName, reason, type, start, end);
                        PunishmentQuerys.createPunishment(punishment);
                    } else {
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Incorrect Id)"))));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Player not found)"))));
                }
            } else if(args.length == 0) {
                sender.sendMessage(new TextComponent(MethodUtil.format("\n" + Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.Ban.Header"))));
                for(int i = 0; i < Data.BAN_REASON_MAP.size(); i++) {
                    String reason = Data.BAN_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[0];
                    String duration = DurationUtil.getDurationString(Data.BAN_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[1]);
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.Ban.Component").replace("%id%", String.valueOf(i + 1)).replace("%reason%", reason)).replace("%duration%", duration)));
                }
                sender.sendMessage(new TextComponent(MethodUtil.format("")));
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }
}
