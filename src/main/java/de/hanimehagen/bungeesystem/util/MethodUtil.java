package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodUtil {

    private final static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String format(String message) {
        Matcher match = pattern.matcher(message);
        while (match.find()) {
            String colour = message.substring(match.start(), match.end());
            message = message.replace(colour, ChatColor.of(colour) + "");
            match = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void loadPunishReasons() {

        Configuration config = Configs.getConfig();

        Collection<String> muteReasonNodes = config.getSection("Reasons.Mute").getKeys();
        Collection<String> banReasonNodes = config.getSection("Reasons.Ban").getKeys();

        for(String key : muteReasonNodes) {
            String value = config.getString("Reasons.Mute." + key + ".Name") + "$" + config.getString("Reasons.Mute." + key + ".Duration");
            Data.MUTE_REASON_MAP.put(key, value);
        }

        for(String key : banReasonNodes) {
            String value = config.getString("Reasons.Ban." + key + ".Name") + "$" + config.getString("Reasons.Ban." + key + ".Duration");
            Data.BAN_REASON_MAP.put(key, value);
        }

    }

}
