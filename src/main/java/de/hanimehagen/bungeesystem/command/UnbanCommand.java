package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;


public class UnbanCommand extends Command {

    PunishmentUtil punishmentUtil;

    public UnbanCommand(String name, String permission, Plugin plugin, DataSource dataSource) {
        super("unban", "system.unban");
        punishmentUtil = new PunishmentUtil(plugin, dataSource);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.unban")) {
            if(args.length == 1) {
                this.punishmentUtil.unpunishName(sender, args[0], PunishmentType.BAN, "Punishment.Unban", "Punishment.NotBanned");
            } else if(args.length == 2) {
                if(args[0].equals("id")) {
                    this.punishmentUtil.unpunishId(sender, args[1], PunishmentType.BAN, "Punishment.UnbanId", "Punishment.CantFindBanId");
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/unban id <banid>"))));
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/unban <player> | /unban id <banid>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }
}
