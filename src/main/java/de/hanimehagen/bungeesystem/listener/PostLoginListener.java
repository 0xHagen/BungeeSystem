package de.hanimehagen.bungeesystem.listener;

import de.hanimehagen.bungeesystem.mysql.PlayerBaseQuerys;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPreLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayerBaseQuerys.deleteNotMatchingPlayerName(player);
        PlayerBaseQuerys.createPlayer(player);
    }

}
