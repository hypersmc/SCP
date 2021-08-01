package scp_minigame.scpminigame.bungeehandler;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import scp_minigame.scpminigame.Main;

public class BungeeHandler {

    public void sendPlayer(Player parm) {
        Player player = (Player) parm;
        if (parm.isOnline()){
            if (Main.main.getConfig().getBoolean("usebungee")) {

            /*for (int i = 0; i < players.size(); i++){
                for (String finalPlayer : players){
                    if (finalPlayer.contains(player.getName())){
                        String thist = finalPlayer;
                        thist.replace(finalPlayer, "");

                    }
                }
                players.remove(player);
            }*/
                ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                dataOutput.writeUTF("Connect");
                dataOutput.writeUTF(Main.main.getConfig().getString("fallback"));
                player.sendPluginMessage(Main.main, "BungeeCord", dataOutput.toByteArray());
            }else {
                player.sendMessage(Main.main.getConfig().getString("prefix") + " Sorry cannot send you to any server.");
                player.sendMessage(Main.main.getConfig().getString("prefix") + " If you believe this is an error please contact the server Administrator!");

            }
        }
    }
}
