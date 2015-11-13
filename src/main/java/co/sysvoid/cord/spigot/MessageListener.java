package co.sysvoid.cord.spigot;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener
{

    public void onPluginMessageReceived(String channel, Player player, byte[] message)
    {
        if (!channel.equals("Cord"))
        {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Power"))
        {
            // Cord [Power] -> {{SERVER}}
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(in.readUTF()); // <-- {{SERVER}}

            for (Player p : Bukkit.getServer().getOnlinePlayers())
            {
                p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
            }

            // Shutdown the server after about 5 seconds. Just to make sure *most* of the
            // players have been moved to another server.
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
            {
                public void run()
                {
                    Bukkit.getServer().shutdown();
                }
            }, 100L);
        }
    }

}
