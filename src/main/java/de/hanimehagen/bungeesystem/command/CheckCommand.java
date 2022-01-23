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
import org.checkerframework.checker.signedness.qual.SignedPositiveFromUnsigned;

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
                        StringBuilder mutedMessage = new StringBuilder();
                        List<String> muteLayout = Configs.getMessages().getStringList("Punishment.Check.Muted");

                        String reason = PunishmentQuerys.getReasonByPunishedUuid(uuid, PunishmentType.MUTE);
                        String operatorUuid = PunishmentQuerys.getOperatorUuidByPunishedUuid(uuid, PunishmentType.MUTE);
                        String operator;

                        assert operatorUuid != null;
                        if(!operatorUuid.equals("CONSOLE")) {
                            if(PlayerQuerys.existsUuid(operatorUuid)) {
                                operator = PlayerQuerys.getName(operatorUuid);
                            } else {
                                operator = PunishmentQuerys.getOperatorByPunishedUuid(uuid, PunishmentType.MUTE);
                            }
                        } else {
                            operator = "CONSOLE";
                        }

                        Timestamp timestampStart = new Timestamp(PunishmentQuerys.getStartTimeByUuid(uuid, PunishmentType.MUTE));
                        Timestamp timestampEnd = new Timestamp(PunishmentQuerys.getEndTimeByUuid(uuid, PunishmentType.MUTE));

                        for (String component : muteLayout) {
                            assert reason != null;
                            assert operator != null;
                            mutedMessage.append(Data.PUNISH_PREFIX).append(component.replace("%reason%", reason).replace("%operator%", operator).replace("%start%", Data.DATE_FORMAT.format(timestampStart)).replace("%end%", Data.DATE_FORMAT.format(timestampEnd))).append("\n");
                        }

                        sender.sendMessage(new TextComponent(MethodUtil.format(mutedMessage.toString())));

                    } else {
                        String notMuted = Configs.getMessages().getString("Punishment.Check.NotMuted");
                        sender.sendMessage(new TextComponent(MethodUtil.format(Data.PUNISH_PREFIX + notMuted + "\n")));
                    }

                    if(isBanned) {
                        StringBuilder bannedMessage = new StringBuilder();
                        List<String> bannedLayout = Configs.getMessages().getStringList("Punishment.Check.Banned");

                        String reason = PunishmentQuerys.getReasonByPunishedUuid(uuid, PunishmentType.BAN);
                        String operatorUuid = PunishmentQuerys.getOperatorUuidByPunishedUuid(uuid, PunishmentType.BAN);
                        String operator;

                        assert operatorUuid != null;
                        if(!operatorUuid.equals("CONSOLE")) {
                            if(PlayerQuerys.existsUuid(operatorUuid)) {
                                operator = PlayerQuerys.getName(operatorUuid);
                            } else {
                                operator = PunishmentQuerys.getOperatorByPunishedUuid(uuid, PunishmentType.BAN);
                            }
                        } else {
                            operator = "CONSOLE";
                        }

                        Timestamp timestampStart = new Timestamp(PunishmentQuerys.getStartTimeByUuid(uuid, PunishmentType.BAN));
                        Timestamp timestampEnd = new Timestamp(PunishmentQuerys.getEndTimeByUuid(uuid, PunishmentType.BAN));

                        System.out.println(reason);
                        System.out.println(operator);
                        System.out.println(Data.DATE_FORMAT.format(timestampStart));
                        System.out.println(Data.DATE_FORMAT.format(timestampEnd));

                        for (String component : bannedLayout) {
                            assert reason != null;
                            assert operator != null;
                            bannedMessage.append(Data.PUNISH_PREFIX).append(component.replace("%reason%", reason).replace("%operator%", operator).replace("%start%", Data.DATE_FORMAT.format(timestampStart)).replace("%end%", Data.DATE_FORMAT.format(timestampEnd))).append("\n");
                        }

                        sender.sendMessage(new TextComponent(MethodUtil.format(bannedMessage.toString())));

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
}
