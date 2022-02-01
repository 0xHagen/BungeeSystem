package de.hanimehagen.bungeesystem;

import de.hanimehagen.bungeesystem.util.ConfigUtil;
import net.md_5.bungee.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Data {

    public static HashMap<String, String> MUTE_REASON_MAP = new HashMap<>();
    public static HashMap<String, String> BAN_REASON_MAP = new HashMap<>();

    private static final Configuration config = ConfigUtil.getConfig();
    private static final Configuration messages = ConfigUtil.getMessages();

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public static final String PREFIX = messages.getString("Prefix");
    public static final String NO_PERMS = messages.getString("NoPerms");
    public static final String CORRECT_USE = messages.getString("CorrectUse");
    public static final String PUNISH_PREFIX = messages.getString("Punishment.Prefix");
    public static final String ERROR = messages.getString("Error");

    public static HashMap<String, Long> BANNED_PLAYERS = new HashMap<>();
    public static HashMap<String, Long> MUTED_PLAYERS = new HashMap<>();
}
