package co.sysvoid.cord;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class EventListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnectEvent(ServerConnectEvent e)
    {
        if (!e.getPlayer().hasPermission("cord.maintenance.join") && Helper.getMaintenance())
        {
            e.getPlayer().disconnect(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append(
                                    Helper.getLang("in_maintenance", "We're currently in maintenance mode!")
                            ).color(ChatColor.GRAY)
                            .create()
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerSwitchEvent(ServerSwitchEvent e)
    {
        if (!e.getPlayer().hasPermission("cord.maintenance.join") && Helper.getMaintenance())
        {
            e.getPlayer().disconnect(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append(
                                    Helper.getLang("in_maintenance", "We're currently in maintenance mode!")
                            ).color(ChatColor.GRAY)
                            .create()
            );
        }
    }

}
