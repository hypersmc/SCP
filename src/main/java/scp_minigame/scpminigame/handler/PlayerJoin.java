package scp_minigame.scpminigame.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoin implements Listener {
    @EventHandler
    public void playerjoin(PlayerJoinEvent event){
        String player = event.getPlayer().getName();
        //String getserver = Main.get

    }


}
