package de.hanimehagen.bungeesystem;

import net.md_5.bungee.config.Configuration;

import java.util.HashMap;

public class Data {

    public static HashMap<String, String> MUTE_REASON_MAP = new HashMap<>();
    public static HashMap<String, String> BAN_REASON_MAP = new HashMap<>();

    public static final Configuration config = Configs.getConfig();
    public static final Configuration messages = Configs.getMessages();

    public static final String PREFIX = messages.getString("Prefix");
    public static final String NO_PERMS = messages.getString("NoPerms");
    public static final String CORRECT_USE = messages.getString("CorrectUse");
    public static final String PUNISH_PREFIX = messages.getString("Punishment.Prefix");

}
