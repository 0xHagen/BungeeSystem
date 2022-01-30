package de.hanimehagen.bungeesystem.listener;

import de.hanimehagen.bungeesystem.Data;
import de.hanimehagen.bungeesystem.data.PlayerBaseData;
import de.hanimehagen.bungeesystem.util.MethodUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.slf4j.event.Level;

import javax.sql.DataSource;

public class PostLoginListener implements Listener {

    private final PlayerBaseData playerBaseData;

    public PostLoginListener(Plugin plugin, DataSource dataSource) {
        this.playerBaseData = new PlayerBaseData(plugin, dataSource);
    }

    @EventHandler
    public void onPreLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        playerBaseData.deleteNotValidPlayer(player);
        playerBaseData.createPlayer(player);
    }

}
