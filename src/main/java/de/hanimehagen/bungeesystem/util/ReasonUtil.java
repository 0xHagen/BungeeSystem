package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Configs;
import de.hanimehagen.bungeesystem.Data;
import net.md_5.bungee.config.Configuration;

import java.util.Collection;

public class ReasonUtil {

    public static void loadReasons() {

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
