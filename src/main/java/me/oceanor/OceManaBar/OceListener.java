package me.oceanor.OceManaBar;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.nisovin.magicspells.events.ManaChangeEvent;

public class OceListener implements Listener 
{
    public OceManaBar plugin;
    
    public OceListener(OceManaBar instance)
    {
        this.plugin=instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onManaChange(ManaChangeEvent event)
    {
        if(OceManaBar.enabled && event.getPlayer().hasPermission("ocemanabar.show"))
        {
            BarOptions opt = OceManaBar.pMapConfig.get(event.getPlayer().getName());
            
            if(OceManaBar.useTexture)
            {
                OceManaBar.gradientbars.get(event.getPlayer()).setWidth(event.getNewAmount() * (opt.getWidth() -3) / event.getMaxMana()).setDirty(true);
            }
            if(OceManaBar.useAscii)
            {
                int segments = (int) (Math.floor(event.getNewAmount() * OceManaBar.size) / event.getMaxMana());
                String textbar = "";
                textbar += ChatColor.DARK_GRAY + "[" + ChatColor.BLUE;
                
                int i;
                for (i=0 ; i<segments; i++)
                    textbar += OceManaBar.segmentChar;
                
                textbar += ChatColor.BLACK;
                
                for (; i< OceManaBar.size; i++)
                    textbar += OceManaBar.segmentChar;
                
                textbar += ChatColor.DARK_GRAY + "]";
            
                OceManaBar.asciibars.get(event.getPlayer()).setText(textbar).setDirty(true);
            }
            if(OceManaBar.showNumeric)
            {
                String numvalues = ChatColor.WHITE + "[" + event.getNewAmount() + "/" + event.getMaxMana() + "]";
                OceManaBar.numericmanas.get(event.getPlayer()).setText(numvalues).setDirty(true);
            }
        }
    }
}
