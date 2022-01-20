package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerQuerys;
import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.DurationUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

    public BanCommand(String name, String permission) {
        super("ban", "system.ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 2) {
            if(PlayerQuerys.existsName(args[0])) {
                if(Data.BAN_REASON_MAP.containsKey(args[1])) {
                    Punishment punishment;
                    String id = PunishmentUtil.generateId();
                    //TODO: UUID updating.
                    String uuid;
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
                    long start = System.currentTimeMillis() / 1000;
                    long end = start + DurationUtil.getDuration(Data.BAN_REASON_MAP.get(args[1]).split("\\$")[1]);
                } else {
                    sender.sendMessage(new TextComponent(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Incorrect Id)")));
                }
            } else {
                sender.sendMessage(new TextComponent(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/ban <player> <id> (Player not found)")));
            }
        } else {
            sender.sendMessage(new TextComponent(Data.PREFIX + Data.NO_PERMS));
        }
    }
}
