package me.oceanor.OceManaBar;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.RenderPriority;
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
        
        if(OceManaBar.enabled && player.hasPermission("ocemanabar.show"))
        {
            GenericLabel numericMana = new GenericLabel();
            GenericLabel asciiBar = new GenericLabel();
            GenericGradient gradientBar = new GenericGradient();
            GenericGradient gradientBackground = new GenericGradient();
        
            if(OceManaBar.useTexture)
            {
                Color lightblue = new Color(0,191,255);
                Color darkblue = new Color(16,78,139);
                Color black = new Color(0,0,0);

                // gradient bar
                gradientBar.setX(OceManaBar.posX +1).setY(OceManaBar.posY +2).setWidth(OceManaBar.width -3).setHeight(OceManaBar.height - 7);
                gradientBar.setBottomColor(lightblue).setTopColor(darkblue).setPriority(RenderPriority.Lowest);

                player.getMainScreen().attachWidget(plugin, gradientBar);
                OceManaBar.gradientbars.put(player,gradientBar);
                
                // black background / frame
                gradientBackground.setX(OceManaBar.posX).setY(OceManaBar.posY).setWidth(OceManaBar.width).setHeight(OceManaBar.height -3);
                gradientBackground.setBottomColor(black).setTopColor(black).setPriority(RenderPriority.Highest);

                player.getMainScreen().attachWidget(plugin, gradientBackground);
                OceManaBar.backgrounds.put(player,gradientBackground);
            }
            
            if(OceManaBar.useAscii)
            {
                asciiBar.setAuto(false).setX(OceManaBar.posX).setY(OceManaBar.posY).setWidth(OceManaBar.width).setHeight(OceManaBar.height);

                String textbar = "";
                textbar += ChatColor.DARK_GRAY + "[" + ChatColor.BLUE;
                int i;
                for (i=0 ; i<OceManaBar.size; i++)
                    textbar += OceManaBar.segmentChar;
                textbar += ChatColor.DARK_GRAY + "]";
                asciiBar.setText(textbar);
                
                player.getMainScreen().attachWidget(plugin, asciiBar);
                OceManaBar.asciibars.put(player,asciiBar);
            }
            
            if(OceManaBar.showNumeric)
            {
                numericMana.setAuto(false).setX(OceManaBar.posX + OceManaBar.width).setY(OceManaBar.posY).setWidth(35).setHeight(OceManaBar.height);
                numericMana.setText("[100"+ "/" + "100]");
                player.getMainScreen().attachWidget(plugin, numericMana);
                OceManaBar.numericmanas.put(player,numericMana);
            }
        }
    }
}
