package co.sysvoid.cord;

import co.sysvoid.cord.commands.CommandCord;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin
{

    public void onEnable()
    {
        getLogger().info("Enabled");
        Helper.setPlugin(this);

        getProxy().getPluginManager().registerListener(this, new EventListener());

        getProxy().getPluginManager().registerCommand(this, new CommandCord());
    }

    public void onDisable()
    {
        getLogger().info("Disabled");
    }

}
