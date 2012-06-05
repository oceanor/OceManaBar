package me.oceanor.OceManaBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OceManaBarSpoutListener implements Listener
{
    public OceManaBar plugin;
    
    public OceManaBarSpoutListener(OceManaBar plugin) 
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpoutCraftEnable(SpoutCraftEnableEvent event)
    {
        SpoutPlayer player = event.getPlayer();
        Utils util = new Utils(plugin);

        if(!OceManaBar.pMapConfig.containsKey(player.getName()))
        {
            BarOptions tmpOpt = new BarOptions(OceManaBar.posX, OceManaBar.posY);
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
}