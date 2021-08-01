package scp_minigame.scpminigame.texturepackhandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import scp_minigame.scpminigame.Main;

public class texturepackhandler implements Listener {

    String url = Main.main.getConfig().getString("fullurl");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent event){
        try {

            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED){
                event.getPlayer().kickPlayer("Texturepack Declined! Please accept!");
            }else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD){
                event.getPlayer().sendMessage("Texturepack failed downloading! Please retry!");
                event.getPlayer().setResourcePack(url);
            }else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED || event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED){
                event.getPlayer().sendMessage("Thanks for accepting texturepack! Have fun!");
            }
        } catch (Exception e) {
            event.getPlayer().kickPlayer("Some kind of error have occurred!");
            e.printStackTrace();
        }
    }
    @EventHandler
    public void playerjoinevent(PlayerJoinEvent event){

        event.getPlayer().setTexturePack(url);

    }
}
