package co.sysvoid.cord.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.net.InetSocketAddress;

public class Helper
{

    private static Main plugin;
    private static boolean maintenance = false;

    public static Main getPlugin()
    {
        return plugin;
    }

    public static void setPlugin(Main plugin)
    {
        Helper.plugin = plugin;
    }

    public static boolean getMaintenance()
    {
        return maintenance;
    }

    public static void setMaintenance(boolean maintenance)
    {
        Helper.maintenance = maintenance;
    }

    public static String getPrefix(String label)
    {
        return ChatColor.BLUE + label + "> " + ChatColor.GRAY;
    }

    public static void noPermissions(ProxiedPlayer player)
    {
        player.sendMessage(
                new ComponentBuilder(Helper.getPrefix("Cord"))
                        .append("You do not have the required permissions to do that!").color(ChatColor.GRAY)
                        .create()
        );
    }

    public static boolean addServer(String name, String host, int port)
    {
        if (ProxyServer.getInstance().getServers().containsKey(name))
        {
            return false;
        }

        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), "", false);
        ProxyServer.getInstance().getServers().put(name, serverInfo);

        try
        {
            Configuration bungeeConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
            bungeeConfig.set("servers." + name + ".address", host + ":" + port);
            bungeeConfig.set("servers." + name + ".restricted", false);
            bungeeConfig.set("servers." + name + ".motd", "");
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(bungeeConfig, new File("config.yml"));
        } catch (Exception e)
        {
            e.printStackTrace();
            ProxyServer.getInstance().getLogger().severe("Couldn't open Bungee config! Send the mess above to Lie (on SpigotMC).");
            return false;
        }

        return true;
    }

    public static boolean removeServer(String name)
    {
        if (!ProxyServer.getInstance().getServers().containsKey(name))
        {
            return false;
        }

        for (ProxiedPlayer plyr : ProxyServer.getInstance().getServers().get(name).getPlayers())
        {
            plyr.disconnect(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("Server was cleared, please rejoin.").color(ChatColor.GRAY)
                            .create()
            );
        }

        ProxyServer.getInstance().getServers().remove(name);

        try
        {
            Configuration bungeeConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
            bungeeConfig.set("servers." + name, null);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(bungeeConfig, new File("config.yml"));
        } catch (Exception e)
        {
            e.printStackTrace();
            ProxyServer.getInstance().getLogger().severe("Couldn't open Bungee config! Send the mess above to Lie (on SpigotMC).");
            return false;
        }

        return true;
    }

    public static String getLang(String key, String fallback)
    {
        File configFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!configFile.exists())
        {
            try
            {
                if (!(configFile.getParentFile().mkdirs() && configFile.createNewFile()))
                {
                    System.out.println("Could not create lang.yml!");
                }
            } catch (IOException e)
            {
                throw new RuntimeException("Could not create lang.yml!", e);
            }
        }

        try
        {
            Configuration bungeeConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "lang.yml"));
            return bungeeConfig.getString(key, fallback);
        } catch (Exception e)
        {
            e.printStackTrace();
            return fallback;
        }
    }

}
