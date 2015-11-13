package co.sysvoid.cord.bungee;

import co.sysvoid.cord.bungee.commands.CommandCord;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin
{

    public void onEnable()
    {
        getLogger().info("Enabled");
        Helper.setPlugin(this);

        getProxy().registerChannel("BungeeCord");
        getProxy().registerChannel("Cord");
        
        getProxy().getPluginManager().registerListener(this, new EventListener());
        getProxy().getPluginManager().registerCommand(this, new CommandCord());
    }

    public void onDisable()
    {
        getLogger().info("Disabled");
    }

}
