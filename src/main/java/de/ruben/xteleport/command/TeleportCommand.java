package de.ruben.xteleport.command;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.ruben.xteleport.XTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    private final XTeleport xTeleport;

    public TeleportCommand(XTeleport xTeleport) {
        this.xTeleport = xTeleport;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(!player.hasPermission("addictzone.command.tp")){
            player.sendMessage("§cDazu hast du keine Rechte!");
            return true;
        }

        if(args.length == 1){
            String targetPlayerName = args[0];
            Player target = Bukkit.getPlayer(targetPlayerName);
            if(target == null || !target.isOnline()){
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(targetPlayerName);
                out.writeUTF(player.getUniqueId().toString());
                player.sendPluginMessage(xTeleport, "xteleport:singleteleportrequest", out.toByteArray());
                System.out.println("Sended Data!");
            }else{
                player.teleport(target);
                player.sendMessage("§7Du hast dich zu §b"+target.getName()+" §7teleportiert!");
            }
        }else if(args.length == 2){

            Player target1 = Bukkit.getPlayer(args[0]);
            Player target2 = Bukkit.getPlayer(args[1]);

            if(target1 == null || target2 == null || !target1.isOnline() || !target2.isOnline()){

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(args[0]);
                out.writeUTF(args[1]);
                out.writeUTF(player.getUniqueId().toString());
                player.sendPluginMessage(xTeleport, "xteleport:targettedteleportrequest", out.toByteArray());
                System.out.println("Dual sent!");
            }else{
                target1.teleport(target2);
                player.sendMessage("§7Du hast §b"+target1.getName()+" §7zu §b"+target2.getName()+" §7teleportiert!");
            }

        }else if(args.length == 3){
            int x = isInteger(args[0]) ? Integer.valueOf(args[0]) : 0;
            int z = isInteger(args[2]) ? Integer.valueOf(args[2]) : 0;

            int y = 0;
            if(args[1].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("height")){
                y = player.getWorld().getHighestBlockYAt(x, z)+2;
            }else{
                y = isInteger(args[1]) ? Integer.valueOf(args[1]) : 100;
            }

            player.teleport(new Location(player.getWorld(),x,y,z));
            player.sendMessage("§7Du wurdest zu §bx="+x+" y="+y+" z="+z+" §7in deiner Welt Teleportiert.");
        }else if(args.length == 4){
            String world = args[0];

            if(Bukkit.getWorld(world) == null){
                player.sendMessage("§7Du hast eine ungültige Welt angegeben!");
                return true;
            }

            int x = isInteger(args[1]) ? Integer.valueOf(args[1]) : 0;
            int z = isInteger(args[3]) ? Integer.valueOf(args[3]) : 0;

            int y = 0;
            if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("height")){
                y = Bukkit.getWorld(world).getHighestBlockYAt(x, z)+2;
            }else{
                y = isInteger(args[2]) ? Integer.valueOf(args[2]) : 100;
            }

            player.teleport(new Location(Bukkit.getWorld(world),x,y,z));
            player.sendMessage("§7Du wurdest zu §bwelt="+world+" x="+x+" y="+y+" z="+z+" §7teleportiert!");
        }else if(args.length == 5){

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(args[0]);
            out.writeUTF(args[1]);
            out.writeUTF(args[2]);
            out.writeUTF(args[3]);
            out.writeUTF(args[4]);
            out.writeUTF(player.getUniqueId().toString());
            player.sendPluginMessage(xTeleport, "xteleport:serverteleportrequest", out.toByteArray());

        }else{
            player.sendMessage("§cBenutze: /tp <player>");
            player.sendMessage("§cBenutze: /tp <player1> to <player2>");
            player.sendMessage("§cBenutze: /tp <x> <y> <z>");
            player.sendMessage("§cBenutze: /tp <world> <x> <y> <z>");
            player.sendMessage("§cBenutze: /tp <server> <world> <x> <y> <z>");
        }

        return false;
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
