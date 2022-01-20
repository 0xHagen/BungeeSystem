package de.hanimehagen.bungeesystem;

import de.hanimehagen.bungeesystem.mysql.MySQL;
import de.hanimehagen.bungeesystem.punishment.Reason;
import de.hanimehagen.bungeesystem.util.ReasonUtil;
import net.md_5.bungee.api.plugin.Plugin;

public final class Bungeesystem extends Plugin {

    private static Bungeesystem instance;
    private Configs configs;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configs = new Configs();
        configs.loadConfigs();
        ReasonUtil.loadReasons();
        MySQL.connect();
        System.out.println(ReasonUtil.BAN_REASON_MAP);
        System.out.println(ReasonUtil.MUTE_REASON_MAP);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.disconnect();
    }

    public static Bungeesystem getInstance() {
        return instance;
    }

}
