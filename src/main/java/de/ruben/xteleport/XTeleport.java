package de.ruben.xteleport;

import de.ruben.xteleport.command.TeleportCommand;
import de.ruben.xteleport.listener.ChannelListener;
import de.ruben.xteleport.listener.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class XTeleport extends JavaPlugin {

    private ChannelListener channelListener;
    @Override
    public void onEnable() {
        this.channelListener = new ChannelListener(this);

        System.out.println("serverteleportrequest");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "xteleport:singleteleportrequest");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "xteleport:targettedteleportrequest");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "xteleport:serverteleportrequest");

        getServer().getMessenger().registerIncomingPluginChannel(this, "xteleport:singleteleportanswer", channelListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, "xteleport:targettedteleportanswer", channelListener);
        getServer().getMessenger().registerIncomingPluginChannel(this, "xteleport:serverteleportanswer", channelListener);

        getCommand("teleport").setExecutor(new TeleportCommand(this));

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }

    public ChannelListener getChannelListener() {
        return channelListener;
    }
}
