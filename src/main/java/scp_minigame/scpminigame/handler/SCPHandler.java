package scp_minigame.scpminigame.handler;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import scp_minigame.scpminigame.Main;
import scp_minigame.scpminigame.interfaces.SCPinterface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SCPHandler implements SCPinterface {

    ArrayList <String> players = new ArrayList<String>();

    @Override
    public String getScp(Object player) {
        return null;
    }

    @Override
    public String getClass(Object player) {
        return null;
    }

    @Override
    public String getSide(Object player) {
        return null;
    }

    
}
