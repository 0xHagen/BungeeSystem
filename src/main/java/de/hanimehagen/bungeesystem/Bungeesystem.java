package de.hanimehagen.bungeesystem;

import de.hanimehagen.bungeesystem.util.ConfigUtil;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import de.hanimehagen.bungeesystem.util.PunishmentUtil;
import net.md_5.bungee.api.plugin.Plugin;

import javax.sql.DataSource;

public final class Bungeesystem extends Plugin {

    //TODO: replace singleton sith di
    private static Bungeesystem instance;

    @Override
    public void onEnable() {
        instance = this;

        ConfigUtil configs = new ConfigUtil();
        configs.loadConfigs();

        PunishmentUtil.loadPunishReasons();

        DataSource dataSource = MethodUtil.initDataSource(this);
        MethodUtil.initBootQuerys(this, dataSource);

        MethodUtil.initCommands(this, dataSource);
        MethodUtil.initListeners(this, dataSource);
    }

    @Override
    public void onDisable() {
    }

    public static Bungeesystem getInstance() {
        return instance;
    }

}
