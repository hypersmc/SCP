package scp_minigame.scpminigame.util;

import org.bukkit.plugin.java.JavaPlugin;
import scp_minigame.scpminigame.Main;
import scp_minigame.scpminigame.texturepackhandler.texturepackhandler;

public class eventregisters {
    static Main main = JavaPlugin.getPlugin(Main.class);

    public static void GetEvents() {
        main.getServer().getPluginManager().registerEvents(new texturepackhandler(), main);
    }

}
