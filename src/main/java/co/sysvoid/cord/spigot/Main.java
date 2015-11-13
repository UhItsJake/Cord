package co.sysvoid.cord.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

    public void onEnable()
    {
        getLogger().info("Enabled");
    }

    public void onDisable()
    {
        getLogger().info("Disabled");
    }

}
