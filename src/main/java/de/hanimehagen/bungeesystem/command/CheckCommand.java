package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerQuerys;
import de.hanimehagen.bungeesystem.mysql.PunishmentQuerys;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Timestamp;
import java.util.List;

public class CheckCommand extends Command {

    public CheckCommand(String name, String permission) {
        super("check", "system.check");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("system.check")) {
            if(args.length == 1) {
                if (PlayerQuerys.existsName(args[0]) || PlayerQuerys.existsUuid(args[0])) {

                    boolean usedName = args[0].length() <= 16;
                    String uuid;
                    String name;
                    if(usedName) {
                        name = args[0];
                        uuid = PlayerQuerys.getUuid(name);
                    } else {
                        uuid = args[0];
                        name = PlayerQuerys.getName(uuid);
                    }

                    boolean isMuted = PunishmentQuerys.isPunishedByUuid(uuid, PunishmentType.MUTE);
                    boolean isBanned = PunishmentQuerys.isPunishedByUuid(uuid, PunishmentType.BAN);
                    StringBuilder headerMessage = new StringBuilder();
                    List<String> headerLayout = Configs.getMessages().getStringList("Punishment.Check.Header");

                    headerMessage.append("\n");
                    for(String component : headerLayout) {
                        assert name != null;
                        assert uuid != null;
                        headerMessage.append(Data.PUNISH_PREFIX).append(component.replace("%name%", name).replace("%uuid%", uuid)).append("\n");
                    }
                    sender.sendMessage(new TextComponent(MethodUtil.format(headerMessage.toString())));

                    if(isMuted) {
                        sendPunishmentCheck1(sender, uuid, PunishmentType.MUTE, "Punishment.Check.Muted");
                    } else {
                        String notMuted = Configs.getMessages().getString("Punishment.Check.NotMuted");
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + notMuted + "\n")));
                    }
                    if(isBanned) {
                        sendPunishmentCheck1(sender, uuid, PunishmentType.BAN, "Punishment.Check.Banned");
                    } else {
                        String notBanned = Configs.getMessages().getString("Punishment.Check.NotBanned");
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + notBanned + "\n")));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/check <player> (Player not found)"))));
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/check <player>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }

    private void sendPunishmentCheck1(CommandSender sender, String uuid, PunishmentType type, String layoutPath) {
        StringBuilder message = new StringBuilder();
        List<String> layout = Configs.getMessages().getStringList(layoutPath);

        String reason = PunishmentQuerys.getReasonByPunishedUuid(uuid, type);
        String operatorUuid = PunishmentQuerys.getOperatorUuidByPunishedUuid(uuid, type);
        String operator;

        assert operatorUuid != null;
        if(!operatorUuid.equals("CONSOLE")) {
            if(PlayerQuerys.existsUuid(operatorUuid)) {
                operator = PlayerQuerys.getName(operatorUuid);
            } else {
                operator = PunishmentQuerys.getOperatorByPunishedUuid(uuid, type);
            }
        } else {
            operator = "CONSOLE";
        }

        Timestamp timestampStart = new Timestamp(PunishmentQuerys.getStartTimeByUuid(uuid, type));
        long endLong = PunishmentQuerys.getEndTimeByUuid(uuid, type);
        String endString;
        if(endLong == -1) {
            endString = "Permanent";
        } else {
            endString = Data.DATE_FORMAT.format(new Timestamp(endLong));
        }

        for(String component : layout) {
            assert reason != null;
            assert operator != null;
            message.append(Data.PUNISH_PREFIX).append(component.replace("%reason%", reason).replace("%operator%", operator).replace("%start%", Data.DATE_FORMAT.format(timestampStart)).replace("%end%", endString)).append("\n");
        }

        sender.sendMessage(new TextComponent(MethodUtil.format(message.toString())));

    }
}