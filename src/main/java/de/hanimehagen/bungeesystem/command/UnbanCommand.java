package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerQuerys;
import de.hanimehagen.bungeesystem.mysql.PunishmentQuerys;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class UnbanCommand extends Command {

    public UnbanCommand(String name, String permission) {
        super("unban", "system.unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.unmute")) {
            String operatorName;
            if(sender instanceof ProxiedPlayer) {
                operatorName = sender.getName();
            } else {
                operatorName = "CONSOLE";
            }
            if(args.length == 1) {
                String uuid = PlayerQuerys.getUuid(args[0]);
                if(PunishmentQuerys.isPunishedByUuid(uuid, PunishmentType.BAN)) {
                    PunishmentQuerys.deletePunishmentByUuid(uuid, PunishmentType.BAN);
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.Unban")).replace("%player%", args[0]).replace("%operator%", operatorName)));
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.NotBanned")).replace("%player%", args[0])));
                }
            } else if(args.length == 2) {
                if(args[0].equals("banid")) {
                    if(PunishmentQuerys.existsId(args[1]) && Objects.equals(PunishmentQuerys.getTypeByPunishId(args[1]), PunishmentType.BAN)) {
                        PunishmentQuerys.deletePunishmentByPunishId(args[1], PunishmentType.BAN);
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.UnbanId")).replace("%id%", args[1]).replace("%operator%", operatorName)));
                    } else {
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.CantFindBanId")).replace("%id%", args[1])));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/unban <player> | /unmute banid <banid>"))));
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/unban <player> | /unmute banid <banid>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }
}
