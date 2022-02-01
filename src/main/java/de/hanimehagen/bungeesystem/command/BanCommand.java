package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.util.ConfigUtil;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.DurationUtil;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;

public class BanCommand extends Command {

    PlayerBaseData playerBaseData;
    PunishmentUtil punishmentUtil;

    public BanCommand(String name, String permission, Plugin plugin, DataSource dataSource) {
        super("ban", "system.ban");
        playerBaseData = new PlayerBaseData(plugin, dataSource);
        punishmentUtil = new PunishmentUtil(plugin, dataSource);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.ban")) {
            if (args.length == 2) {
                if (this.playerBaseData.existsName(args[0])) {
                    if (Data.BAN_REASON_MAP.containsKey(args[1])) {
                        this.punishmentUtil.punish(sender, args[0], args[1], PunishmentType.BAN, Data.BAN_REASON_MAP, "Punishment.AlreadyBanned", "Punishment.Ban");
                    } else {
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Incorrect Id)"))));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Player not found)"))));
                }
            } else if(args.length == 0) {
                sender.sendMessage(new TextComponent(MethodUtil.format("\n" + Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString("Punishment.BanReasons.Header"))));
                for(int i = 0; i < Data.BAN_REASON_MAP.size(); i++) {
                    String reason = Data.BAN_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[0];
                    String duration = DurationUtil.getDurationString(Data.BAN_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[1]);
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + ConfigUtil.getMessages().getString("Punishment.BanReasons.Component").replace("%id%", String.valueOf(i + 1)).replace("%reason%", reason)).replace("%duration%", duration)));
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
