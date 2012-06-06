package me.oceanor.OceManaBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OceManaBarSpoutListener implements Listener
{
    public OceManaBar plugin;
    public Utils util;
    
    public OceManaBarSpoutListener(OceManaBar plugin) 
    {
        this.plugin = plugin;
        util = new Utils(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
    {
        SpoutPlayer player = event.getPlayer();
        OceManaBar.SpoutPlayers.add(player.getName());

        if(!OceManaBar.pMapConfig.containsKey(player.getName()))
        {
            BarOptions tmpOpt = new BarOptions(OceManaBar.posX, OceManaBar.posY, OceManaBar.width, OceManaBar.height);
            OceManaBar.pMapConfig.put(player.getName(), tmpOpt);
        }

        if(OceManaBar.enabled && player.hasPermission("ocemanabar.show"))
        {
            if(OceManaBar.useTexture)
            {
                util.SetGradientBar(new GenericGradient(), player);
                util.SetBackgroundBar(new GenericGradient(), player);
            }

            if(OceManaBar.useAscii)
            {
                util.setAsciiBar(new GenericLabel(), player);
            }
            
            if(OceManaBar.showNumeric)
            {
                util.setNumericMana(new GenericLabel(), player);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        String pname = event.getPlayer().getName();
        if(OceManaBar.SpoutPlayers.contains(pname))
            OceManaBar.SpoutPlayers.remove(pname);

        OceManaBar.asciibars.remove(pname);
        OceManaBar.gradientbars.remove(pname);
        OceManaBar.backgrounds.remove(pname);
    }
}