package co.sysvoid.cord.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

    private static Main instance;

    public void onEnable()
    {
        getLogger().info("Enabled");
        Main.instance = this;

        getServer().getMessenger().registerOutgoingPluginChannel(this, "Cord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "Cord", new MessageListener());
    }

    public void onDisable()
    {
        getLogger().info("Disabled");
    }

    public static Main getInstance()
    {
        return instance;
    }

}
