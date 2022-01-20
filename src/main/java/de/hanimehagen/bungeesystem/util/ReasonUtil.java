package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Configs;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ReasonUtil {

    public static HashMap<Integer, String> MUTE_REASON_MAP = new HashMap<Integer, String>();
    public static HashMap<Integer, String> BAN_REASON_MAP = new HashMap<Integer, String>();

    public static void loadReasons() {

        Configuration config = Configs.getConfig();

        Collection<String> muteReasonNodes = config.getSection("Reasons.Mute").getKeys();
        Collection<String> banReasonNodes = config.getSection("Reasons.Ban").getKeys();

        for(String key : muteReasonNodes) {
            int id = Integer.parseInt(key);
            String value = config.getString("Reasons.Mute." + key);
            MUTE_REASON_MAP.put(id, value);
        }

        for(String key : banReasonNodes) {
            int id = Integer.parseInt(key);
            String value = config.getString("Reasons.Ban." + key);
            BAN_REASON_MAP.put(id, value);
        }

    }

}
