package de.hanimehagen.bungeesystem;

import de.hanimehagen.bungeesystem.command.BanCommand;
import de.hanimehagen.bungeesystem.command.CheckCommand;
import de.hanimehagen.bungeesystem.command.MuteCommand;
import de.hanimehagen.bungeesystem.command.UnbanCommand;
import de.hanimehagen.bungeesystem.listener.PostLoginListener;
import de.hanimehagen.bungeesystem.mysql.MySQL;
import de.hanimehagen.bungeesystem.util.ReasonUtil;
import net.md_5.bungee.api.plugin.Plugin;

public final class Bungeesystem extends Plugin {

    private static Bungeesystem instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Configs configs = new Configs();
        configs.loadConfigs();
        ReasonUtil.loadReasons();
        MySQL.connect();
        MySQL.createTables();
        init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.disconnect();
    }

    public void init() {
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban", "system.ban"));
        getProxy().getPluginManager().registerCommand(this, new MuteCommand("mute", "system.mute"));
        getProxy().getPluginManager().registerCommand(this, new CheckCommand("check", "system.check"));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban", "system.unban"));
        getProxy().getPluginManager().registerListener(this, new PostLoginListener());
    }

    public static Bungeesystem getInstance() {
        return instance;
    }

}
