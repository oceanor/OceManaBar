package me.oceanor.OceManaBar;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.RenderPriority;

public class OceCommandHandler implements CommandExecutor 
{
    public OceManaBar plugin;
    
    public OceCommandHandler(OceManaBar instance)
    {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
    {
        if(command.getName().equalsIgnoreCase("ocemanabar") || command.getName().equalsIgnoreCase("manabar"))
        {
            if (args.length > 0 && args[0].toLowerCase().equals("reload")) 
            {
                boolean doReload = true;
                if (sender instanceof Player) 
                {
                    if(!(sender.hasPermission("ocemanabar.reload") || sender.hasPermission("ocemanabar.admin")))
                    {
                        sender.sendMessage("You do not have permission to reload.");
                        doReload = false;
                    }
                }
                
                if (doReload) 
                {
                    plugin.reloadConfig();

                    OceManaBar.maxMana = plugin.getConfig().getInt("maxMana");
                    OceManaBar.showNumeric = plugin.getConfig().getBoolean("showNumeric");
                    OceManaBar.posX = plugin.getConfig().getInt("posX");
                    OceManaBar.posY = plugin.getConfig().getInt("posY");
                    OceManaBar.height = plugin.getConfig().getInt("height");
                    OceManaBar.width = plugin.getConfig().getInt("width");
                    OceManaBar.segmentChar = plugin.getConfig().getString("segmentChar");
                    OceManaBar.size = plugin.getConfig().getInt("size");

                    if(OceManaBar.manabarType == 2 && OceManaBar.height < 8)
                        OceManaBar.height = 8;
                    if(OceManaBar.manabarType == 2 && OceManaBar.width < 4)
                        OceManaBar.width = 4;

                    Color lightblue = new Color(0,191,255);
                    Color darkblue = new Color(16,78,139);
                    Color black = new Color(0,0,0);


                    // now let's reload open widgets!
                    // ascii bars
                    Iterator<Entry<Player, GenericLabel>> it1 = OceManaBar.asciibars.entrySet().iterator();
                    while (it1.hasNext())
                    {
                        GenericLabel asciibar = it1.next().getValue();
                        asciibar.setAuto(false).setX(OceManaBar.posX).setY(OceManaBar.posY).setWidth(OceManaBar.width).setHeight(OceManaBar.height);
                    }

                    // gradients
                    Iterator<Entry<Player, GenericGradient>> it2 = OceManaBar.gradientbars.entrySet().iterator();
                    while (it2.hasNext())
                    {
                        GenericGradient bar = it2.next().getValue();
                        bar.setX(OceManaBar.posX +1).setY(OceManaBar.posY +2).setWidth(OceManaBar.width -3).setHeight(OceManaBar.height - 7);
                        bar.setBottomColor(lightblue).setTopColor(darkblue).setPriority(RenderPriority.Lowest);
                    }

                    // backgrounds
                    Iterator<Entry<Player, GenericGradient>> it3 = OceManaBar.backgrounds.entrySet().iterator();
                    while (it3.hasNext())
                    {
                        GenericGradient bg = it3.next().getValue();
                        bg.setX(OceManaBar.posX).setY(OceManaBar.posY).setWidth(OceManaBar.width).setHeight(OceManaBar.height -3);
                        bg.setBottomColor(black).setTopColor(black).setPriority(RenderPriority.Highest);
                    }

                    // numeric manas
                    Iterator<Entry<Player, GenericLabel>> it4 = OceManaBar.numericmanas.entrySet().iterator();
                    while (it4.hasNext())
                    {
                        GenericLabel numericmana = it4.next().getValue();
                        numericmana.setAuto(false).setX(OceManaBar.posX + OceManaBar.width).setY(OceManaBar.posY).setWidth(35).setHeight(OceManaBar.height);
                    }

                    sender.sendMessage("OceManaBar Configuration Reloaded.");
                    return true;
                }
            }
        }
        return false;
    }
}
