package de.hanimehagen.bungeesystem;

import de.hanimehagen.bungeesystem.command.*;
import de.hanimehagen.bungeesystem.data.DataSourceProvider;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.data.PunishmentData;
import de.hanimehagen.bungeesystem.listener.PostLoginListener;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Level;

public final class Bungeesystem extends Plugin {

    private static Bungeesystem instance;
    private DataSource dataSource;
    PlayerBaseData playerBaseData;
    PunishmentData punishmentData;

    @Override
    public void onEnable() {
        instance = this;

        Configs configs = new Configs();
        configs.loadConfigs();
        MethodUtil.loadPunishReasons();

        //TODO: Put in extra method
        try {
            dataSource = DataSourceProvider.initMySQLDataSource(this);
        } catch (SQLException e) {
            this.getLogger().log(Level.SEVERE, "Could not establish database connection", e);
        }

        playerBaseData = new PlayerBaseData(this, dataSource);
        punishmentData = new PunishmentData(this, dataSource);
        playerBaseData.createTable();
        punishmentData.createTable();

        init();
    }

    @Override
    public void onDisable() {
    }

    public void init() {
        getProxy().getPluginManager().registerCommand(this, new BanCommand("ban", "system.ban", this, dataSource));
        getProxy().getPluginManager().registerCommand(this, new MuteCommand("mute", "system.mute", this, dataSource));
        getProxy().getPluginManager().registerCommand(this, new CheckCommand("check", "system.check", this, dataSource));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand("unban", "system.unban", this, dataSource));
        getProxy().getPluginManager().registerCommand(this, new UnmuteCommand("unmute", "system.unmute", this, dataSource));
        getProxy().getPluginManager().registerListener(this, new PostLoginListener(this, dataSource));
    }

    public static Bungeesystem getInstance() {
        return instance;
    }

}
