package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerQuerys;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.DurationUtil;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

    public MuteCommand(String name, String permission) {
        super("mute", "system.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.mute")) {
            if (args.length == 2) {
                if (PlayerQuerys.existsName(args[0])) {
                    if (Data.MUTE_REASON_MAP.containsKey(args[1])) {
                        PunishmentUtil.punish(sender, args[0], args[1], PunishmentType.MUTE, Data.MUTE_REASON_MAP, "Punishment.AlreadyMuted", "Punishment.Mute");
                    } else {
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/mute <player> <id> (Incorrect Id)"))));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/mute <player> <id> (Player not found)"))));
                }
            } else if(args.length == 0) {
                sender.sendMessage(new TextComponent(MethodUtil.format("\n" + Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.MuteReasons.Header"))));
                for(int i = 0; i < Data.MUTE_REASON_MAP.size(); i++) {
                    String reason = Data.MUTE_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[0];
                    String duration = DurationUtil.getDurationString(Data.MUTE_REASON_MAP.get(String.valueOf(i + 1)).split("\\$")[1]);
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.MuteReasons.Component").replace("%id%", String.valueOf(i + 1)).replace("%reason%", reason)).replace("%duration%", duration)));
                }
                sender.sendMessage(new TextComponent(MethodUtil.format("")));
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/mute <player> <id>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }
}
