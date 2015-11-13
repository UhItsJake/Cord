package co.sysvoid.cord.bungee.commands;

import co.sysvoid.cord.bungee.Helper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandCord extends Command
{

    public CommandCord()
    {
        super("cord");
    }

    public void usage(ProxiedPlayer player)
    {
        if (player.hasPermission("cord.servers"))
        {
            player.sendMessage(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("/cord server add <name> <host:port> - " + Helper.getLang("adds_server", "Adds a server") + ".").color(ChatColor.GRAY)
                                            .create()
                            );

            player.sendMessage(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("/cord server remove <name> - " + Helper.getLang("removes_server", "Removes a server") + ".").color(ChatColor.GRAY)
                                            .create()
            );

            player.sendMessage(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("/cord server clear <name> [fallback] - " + Helper.getLang("clear_fallback", "Clears players from a server [and sends players to the fallback server]") + ".").color(ChatColor.GRAY)
                            .create()
            );

            player.sendMessage(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("/cord server shutdown <name> <fallback> - " + Helper.getLang("shutdown_fallback", "Powers off a server, and sends the players to a fallback server") + ".").color(ChatColor.GRAY)
                            .create()
            );
        }

        if (player.hasPermission("cord.maintenance.toggle"))
        {
            player.sendMessage(
                    new ComponentBuilder(Helper.getPrefix("Cord"))
                            .append("/cord maintenance - " + Helper.getLang("toggles_maintenance", "Toggles maintenance mode") + ".").color(ChatColor.GRAY)
                            .create()
            );
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (sender instanceof ProxiedPlayer)
        {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (args.length == 0)
            {
                usage(player);
            } else if (args[0].equalsIgnoreCase("server"))
            {
                if (player.hasPermission("cord.servers"))
                {
                    if (args[1].equalsIgnoreCase("add") && args.length == 4)
                    {
                        String host = args[3].split(":")[0];
                        int port = 25565;

                        try
                        {
                            String portTmp = args[3].split(":")[1];

                            if (!portTmp.equalsIgnoreCase(""))
                            {
                                port = Integer.parseInt(portTmp);
                            }
                        } catch (Exception e)
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("invalid_port", "Invalid port specified.")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                            return;
                        }

                        if (Helper.addServer(args[2], host, port))
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("added_successfully", "Server added successfully!")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        } else
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("already_exists", "Failed to add server: does it already exist?")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        }
                    } else if (args[1].equalsIgnoreCase("remove") && args.length == 3)
                    {
                        if (Helper.removeServer(args[2]))
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("removed_successfully", "Successfully removed server!")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        } else
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("does_it_exist", "Failed to remove server: does it exist?")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        }
                    } else if (args[1].equalsIgnoreCase("clear") && args.length == 4)
                    {
                        ServerInfo fallback = ProxyServer.getInstance().getServerInfo(args[3]);

                        if (fallback == null)
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("fallback_404", "Fallback server not found.")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                            return;
                        }

                        for (ProxiedPlayer plyr : ProxyServer.getInstance().getServers().get(args[2]).getPlayers())
                        {
                            plyr.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("cleared_fallback", "Your server has been cleared and you have been connected to a fallback server.")
                                            ).color(ChatColor.RED)
                                            .create()
                            );
                            plyr.connect(fallback);
                        }

                        player.sendMessage(
                                new ComponentBuilder(Helper.getPrefix("Cord"))
                                        .append(
                                                Helper.getLang("cleared_successfully", "Successfully cleared server!")
                                        ).color(ChatColor.GRAY)
                                        .create()
                        );
                    } else if (args[1].equalsIgnoreCase("shutdown") && args.length == 4)
                    {
                        ServerInfo fallback = ProxyServer.getInstance().getServerInfo(args[3]);

                        if (fallback == null)
                        {
                            player.sendMessage(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("fallback_404", "Fallback server not found.")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                            return;
                        }

                        // @Todo: Send: Cord [Power] -> Shutdown plugin message.
                        // Please see https://github.com/SysVoid/Cord/blob/master/src/main/java/co/sysvoid/cord/spigot/MessageListener.java
                    } else if (args[1].equalsIgnoreCase("clear") && args.length == 3)
                    {
                        for (ProxiedPlayer plyr : ProxyServer.getInstance().getServers().get(args[2]).getPlayers())
                        {
                            plyr.disconnect(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("cleared_disconnect", "Server was cleared, please rejoin.")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        }

                        player.sendMessage(
                                new ComponentBuilder(Helper.getPrefix("Cord"))
                                        .append(
                                                Helper.getLang("cleared_successfully", "Successfully cleared server!")
                                        ).color(ChatColor.GRAY)
                                        .create()
                        );
                    } else
                    {
                        usage(player);
                    }
                    return;
                }
                Helper.noPermissions(player);
                return;
            } else if (args[0].equalsIgnoreCase("maintenance"))
            {
                if (player.hasPermission("cord.maintenance.toggle"))
                {
                    String mode;
                    ChatColor color;

                    if (!Helper.getMaintenance())
                    {
                        mode = Helper.getLang("enabled", "ENABLED");
                        color = ChatColor.GREEN;
                        Helper.setMaintenance(true);
                    } else
                    {
                        mode = Helper.getLang("disabled", "DISABLED");
                        color = ChatColor.RED;
                        Helper.setMaintenance(false);
                    }

                    ProxyServer.getInstance().broadcast(
                            new ComponentBuilder(Helper.getPrefix("Cord"))
                                    .append(
                                            Helper.getLang("maintenance_has_been", "Maintenance mode has been") + " "
                                    ).color(ChatColor.GRAY)
                                    .append(mode).color(color)
                                    .append(".").color(ChatColor.GRAY)
                                    .create()
                    );

                    for (ProxiedPlayer plyr : ProxyServer.getInstance().getPlayers())
                    {
                        if (!plyr.hasPermission("cord.maintenance.join"))
                        {
                            plyr.disconnect(
                                    new ComponentBuilder(Helper.getPrefix("Cord"))
                                            .append(
                                                    Helper.getLang("in_maintenance", "We're currently in maintenance mode!")
                                            ).color(ChatColor.GRAY)
                                            .create()
                            );
                        }
                    }

                    return;
                }
                Helper.noPermissions(player);
                return;
            } else
            {
                usage(player);
            }
            return;
        }
        sender.sendMessage(
                new ComponentBuilder(Helper.getPrefix("Cord"))
                        .append(
                                Helper.getLang("unavailable_console", "This command is unavailable in the console, sorry!")
                        ).color(ChatColor.GRAY)
                        .create()
        );
    }

}
