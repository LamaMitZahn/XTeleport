package de.ruben.xteleport.listener;

import de.ruben.xteleport.XTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private XTeleport xTeleport;

    public JoinListener(XTeleport xTeleport) {
        this.xTeleport = xTeleport;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(xTeleport.getChannelListener().playersToTeleport.containsKey(player.getUniqueId())){
            Player target = Bukkit.getPlayer(xTeleport.getChannelListener().playersToTeleport.get(player.getUniqueId()));

            if(target == null || !target.isOnline()){
                player.sendMessage("Der Spieler zu dem du dich teleportieren wolltest ist offline gegangen!");
                xTeleport.getChannelListener().playersToTeleport.remove(player.getUniqueId());
                return;
            }

            player.teleport(target);
            xTeleport.getChannelListener().playersToTeleport.remove(player.getUniqueId());

        }else if(xTeleport.getChannelListener().locationTeleports.containsKey(player.getUniqueId())){
            Location location = xTeleport.getChannelListener().locationTeleports.get(player.getUniqueId());

            if(location == null){
                player.sendMessage("§7Deine Eingaben waren fehlerhaft! Du hast vielleicht eine ungültige Welt angegeben!");
                return;
            }

            player.teleport(location);
            player.sendMessage("§7Du wurdest zu §bwelt="+location.getWorld().getName()+" x="+location.getX()+" y="+location.getY()+" z="+location.getZ()+" §7teleportiert!");
        }
    }
}
