package de.hanimehagen.bungeesystem.command;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.mysql.PlayerBaseQuerys;
import de.hanimehagen.bungeesystem.mysql.PunishmentQuerys;
import de.hanimehagen.bungeesystem.punishment.Punishment;
import de.hanimehagen.bungeesystem.punishment.PunishmentType;
import de.hanimehagen.bungeesystem.util.DurationUtil;
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
                if (PlayerBaseQuerys.existsName(args[0]) || PlayerBaseQuerys.existsUuid(args[0])) {

                    boolean usedName = args[0].length() <= 16;
                    String uuid;
                    String name;
                    if(usedName) {
                        name = args[0];
                        uuid = PlayerBaseQuerys.getUuid(name);
                    } else {
                        uuid = args[0];
                        name = PlayerBaseQuerys.getName(uuid);
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
                        sendPunishmentCheck(sender, uuid, PunishmentType.MUTE, "Punishment.Check.Muted");
                    } else {
                        String notMuted = Configs.getMessages().getString("Punishment.Check.NotMuted");
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + notMuted + "\n")));
                    }
                    if(isBanned) {
                        sendPunishmentCheck(sender, uuid, PunishmentType.BAN, "Punishment.Check.Banned");
                    } else {
                        String notBanned = Configs.getMessages().getString("Punishment.Check.NotBanned");
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + notBanned + "\n")));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/check <player> (Player not found)"))));
                }
            } else if(args.length == 2) {
                if(args[0].equals("id")) {
                    if(PunishmentQuerys.existsId(args[1])) {
                        StringBuilder message = new StringBuilder();
                        List<String> layout = Configs.getMessages().getStringList("Punishment.Check.Id");
                        Punishment punishment = PunishmentQuerys.getPunishmentbyId(args[1]);
                        assert punishment != null;
                        String end = DurationUtil.getEndDurationString(punishment.getEndTime());
                        message.append("\n");
                        for(String component : layout) {
                            message.append(Data.PUNISH_PREFIX).append(component.replace("%id%", args[1]).
                                    replace("%uuid%", punishment.getUuid()).
                                    replace("%name%", punishment.getName()).
                                    replace("%operatoruuid%", punishment.getOperatorUuid()).
                                    replace("%operatorname%", punishment.getOperatorName()).
                                    replace("%reason%", punishment.getReason()).
                                    replace("%type%", punishment.getType().toString()).
                                    replace("%start%", Data.DATE_FORMAT.format(new Timestamp(punishment.getStartTime()))).
                                    replace("%end%", end)).append("\n");
                        }
                        sender.sendMessage(new TextComponent(MethodUtil.format(message.toString())));
                    } else {
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + Configs.getMessages().getString("Punishment.CantFindId")).replace("%id%", args[1])));
                    }
                } else {
                    sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/check id <id>"))));
                }
            } else {
                sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.CORRECT_USE.replace("%cmd%", "/check <player> | /check id <id>"))));
            }
        } else {
            sender.sendMessage(new TextComponent(MethodUtil.format(Data.PREFIX + Data.NO_PERMS)));
        }
    }

    private void sendPunishmentCheck(CommandSender sender, String uuid, PunishmentType type, String layoutPath) {
        StringBuilder message = new StringBuilder();
        List<String> layout = Configs.getMessages().getStringList(layoutPath);

        Punishment punishment = PunishmentQuerys.getPunishmentbyUuidAndType(uuid, type);

        assert punishment != null;
        String reason = punishment.getReason();
        String operatorUuid = punishment.getOperatorUuid();
        String operator;

        assert operatorUuid != null;
        if(!operatorUuid.equals("CONSOLE")) {
            operator = punishment.getOperatorName();
        } else {
            operator = "CONSOLE";
        }

        Timestamp timestampStart = new Timestamp(punishment.getStartTime());
        String end = DurationUtil.getEndDurationString(punishment.getEndTime());

        for(String component : layout) {
            assert reason != null;
            assert operator != null;
            message.append(Data.PUNISH_PREFIX).append(component.replace("%reason%", reason).replace("%operator%", operator).replace("%start%", Data.DATE_FORMAT.format(timestampStart)).replace("%end%", end)).append("\n");
        }

        sender.sendMessage(new TextComponent(MethodUtil.format(message.toString())));

    }
}