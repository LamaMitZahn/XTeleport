package de.ruben.xteleport.listener;

import de.ruben.xteleport.XTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChannelListener implements PluginMessageListener {

    private XTeleport xTeleport;
    public final HashMap<UUID, UUID> playersToTeleport;
    public final HashMap<UUID, Location> locationTeleports;

    public ChannelListener(XTeleport xTeleport) {
        this.xTeleport = xTeleport;
        this.playersToTeleport = new HashMap<>();
        this.locationTeleports = new HashMap<>();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(message));

        if(channel.equals("xteleport:singleteleportanswer")){

            try {
                String sendPlayerUUID = dataInputStream.readUTF();

                String targetPlayerUUID = dataInputStream.readUTF();
                playersToTeleport.put(UUID.fromString(sendPlayerUUID), UUID.fromString(targetPlayerUUID));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(channel.equals("xteleport:targettedteleportanswer")){
            try {
                String target1UUID = dataInputStream.readUTF();
                String target2UUID = dataInputStream.readUTF();
                playersToTeleport.put(UUID.fromString(target1UUID), UUID.fromString(target2UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(channel.equals("xteleport:serverteleportanswer")){
            try {
                String world = dataInputStream.readUTF();
                String xString = dataInputStream.readUTF();
                String yString = dataInputStream.readUTF();
                String zString = dataInputStream.readUTF();
                String senderUUID = dataInputStream.readUTF();

                if(Bukkit.getWorld(world) == null){
                    locationTeleports.put(UUID.fromString(senderUUID), null);
                    return;
                }

                World bukkitWorld = Bukkit.getWorld(world);
                int x = isInteger(xString) ? Integer.valueOf(xString) : 0;
                int z = isInteger(zString) ? Integer.valueOf(zString) : 0;
                int y = 0;
                if(yString.equalsIgnoreCase("h") || yString.equalsIgnoreCase("height")){
                    y = bukkitWorld.getHighestBlockYAt(x,z);
                }else{
                    y = isInteger(yString) ? Integer.valueOf(yString) : 100;
                }

                Location toteleport = new Location(bukkitWorld, x, y, z);

                locationTeleports.put(UUID.fromString(senderUUID), toteleport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isInteger(String input){
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
