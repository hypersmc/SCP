package scp_minigame.scpminigame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import scp_minigame.scpminigame.texturepackhandler.web.AcceptedSocketConnection;
import scp_minigame.scpminigame.util.eventregisters;

public final class Main extends JavaPlugin {
    public static Main main;
    public static String closeConnection = "!Close Connection!";
    private int listeningport;
    private Thread acceptor;
    private boolean acceptorRunning;
    private Main m = this;
    private ServerSocket ss;
    private synchronized boolean getAcceptorRunning() {
        return this.acceptorRunning;
    }
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Logger logger = getLogger();
        File file = new File("plugins/WebPlugin/web/index.html");
        if (!(new File(getDataFolder() + "/web/", "index.html")).exists())
            saveResource("web/index.html", false);
        if (!file.exists()) {
            Bukkit.getServer().getLogger().warning("No index for html was found!");
        }

        if (getConfig().isSet("port")) {
            Bukkit.getServer().getLogger().info(ChatColor.GRAY + "Found a listening port!");
            try {
                this.listeningport = getConfig().getInt("port");
                this.ss = new ServerSocket(this.listeningport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (getConfig().contains("listeningport")) {
            Bukkit.getServer().getLogger().warning(ChatColor.YELLOW + "Listening port for WebServer NOT FOUND! Using internal default value!");
            try {
                this.listeningport = getConfig().getInt("port");
                this.ss = new ServerSocket(this.listeningport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getServer().getLogger().warning(ChatColor.DARK_RED + "Plugin disabled! NO VALUE WAS FOUND FOR LISTENING PORT!");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.acceptorRunning = true;
        this.acceptor = new Thread(new Runnable() {
            public void run() {
                Bukkit.getServer().getLogger().info(ChatColor.AQUA + "accepting connections");
                while (Main.this.getAcceptorRunning()) {
                    try {
                        Socket sock = Main.this.ss.accept();
                        (new AcceptedSocketConnection(sock, Main.this.m)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bukkit.getServer().getLogger().info(ChatColor.LIGHT_PURPLE + "Done accepting connections");
            }
        });
        this.acceptor.start();
        eventregisters.GetEvents();

    }

    @Override
    public void onDisable() {
        this.acceptorRunning = false;
        try {
            Socket sockCloser = new Socket("localhost", this.listeningport);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sockCloser.getOutputStream()));
            out.write(closeConnection);
            out.close();
            sockCloser.close();
            Bukkit.getServer().getLogger().info(ChatColor.DARK_GREEN + "Closed listening web server successfully!");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*public static String getServer(Player p)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        ByteArrayDataInput in = ByteStreams.newDataInput(null);
        out.writeUTF("GetServer");
        p.sendPluginMessage(main, "BungeeCord", out.toByteArray());
        String server = in.readUTF();
        return server;
    }*/
}
