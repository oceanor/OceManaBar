package me.oceanor.OceManaBar;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OceCommandHandler implements CommandExecutor 
{
    public OceManaBar plugin;
    Utils util = new Utils(plugin);
    
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
                    if(!(sender.isOp() || sender.hasPermission("ocemanabar.reload") || sender.hasPermission("ocemanabar.admin")))
                    {
                        sender.sendMessage("You do not have permission to reload.");
                        doReload = false;
                    }
                }
                
                if (doReload) 
                {
                    plugin.reloadConfig();

                    int oldType = OceManaBar.manabarType;
                    boolean oldShow = OceManaBar.showNumeric;
                    
                    OceManaBar.enabled = plugin.getConfig().getBoolean("enabled");
                    OceManaBar.manabarType = plugin.getConfig().getInt("manabarType");
                    if(OceManaBar.manabarType == 1)
                    {
                        OceManaBar.useAscii = true;
                        OceManaBar.useTexture = false;
                    }
                    else
                    {
                        OceManaBar.useAscii = false;
                        OceManaBar.useTexture = true;    
                    }
                    
                    OceManaBar.maxMana = plugin.getConfig().getInt("maxMana");
                    OceManaBar.showNumeric = plugin.getConfig().getBoolean("showNumeric");
                    OceManaBar.posX = plugin.getConfig().getInt("posX");
                    OceManaBar.posY = plugin.getConfig().getInt("posY");
                    OceManaBar.height = plugin.getConfig().getInt("height");
                    OceManaBar.width = plugin.getConfig().getInt("width");
                    OceManaBar.gradient1 = OceManaBar.hexToRgb(plugin.getConfig().getString("textureColor1"));
                    OceManaBar.gradient2 = OceManaBar.hexToRgb(plugin.getConfig().getString("textureColor2"));
                    OceManaBar.bgcolor1 = OceManaBar.hexToRgb(plugin.getConfig().getString("backgroundColor1"));
                    OceManaBar.bgcolor2 = OceManaBar.hexToRgb(plugin.getConfig().getString("backgroundColor2"));
                    OceManaBar.segmentChar = plugin.getConfig().getString("segmentChar");
                    OceManaBar.size = plugin.getConfig().getInt("size");

                    if(OceManaBar.manabarType == 2 && OceManaBar.height < 8)
                        OceManaBar.height = 8;
                    if(OceManaBar.manabarType == 2 && OceManaBar.width < 4)
                        OceManaBar.width = 4;


                    // now let's reload open widgets!
                    // ascii bars
                    Iterator<Entry<Player, GenericLabel>> it1 = OceManaBar.asciibars.entrySet().iterator();
                    while (it1.hasNext())
                    {
                        Entry<Player, GenericLabel> item = it1.next();
                        GenericLabel asciibar = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        
                        util.setAsciiBar(asciibar, p);
                        
                        // from ascii to texture, we need to delete ascii labels and initialize gradients / backgrounds
                        if(OceManaBar.enabled && oldType == 1 && OceManaBar.manabarType == 2)
                        {
                            p.getMainScreen().removeWidget(asciibar);
                            OceManaBar.asciibars.remove(item);
                            
                            GenericGradient gradientBar = new GenericGradient();
                            GenericGradient gradientBackground = new GenericGradient();

                            util.SetGradientBar(gradientBar, p);
                            util.SetBackgroundBar(gradientBackground, p);
                        }
                    }

                    // gradients
                    Iterator<Entry<Player, GenericGradient>> it2 = OceManaBar.gradientbars.entrySet().iterator();
                    while (it2.hasNext())
                    {
                        Entry<Player, GenericGradient> item = it2.next();
                        GenericGradient bar = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        util.SetGradientBar(bar, p);
                        
                        // from textures to ASCII, we need to delete gradients and initialize ASCII labels
                        if(OceManaBar.enabled && oldType == 2 && OceManaBar.manabarType == 1)
                        {
                            p.getMainScreen().removeWidget(bar);
                            OceManaBar.gradientbars.remove(item);
                            
                            GenericLabel asciiBar = new GenericLabel();
                            util.setAsciiBar(asciiBar, p);
                        }
                    }

                    // backgrounds
                    Iterator<Entry<Player, GenericGradient>> it3 = OceManaBar.backgrounds.entrySet().iterator();
                    while (it3.hasNext())
                    {
                        Entry<Player, GenericGradient> item = it3.next();
                        GenericGradient bg = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        util.SetBackgroundBar(bg, p);
                        
                        // from textures to ASCII, we just need to delete backgrounds, ASCII labels are already initialized in gradients part 
                        if(OceManaBar.enabled && oldType == 2 && OceManaBar.manabarType == 1)
                        {
                            p.getMainScreen().removeWidget(bg);
                            OceManaBar.backgrounds.remove(item);
                        }
                    }

                    // numeric mana
                    Iterator<Entry<Player, GenericLabel>> it4 = OceManaBar.numericmanas.entrySet().iterator();
                    while (it4.hasNext())
                    {
                        Entry<Player, GenericLabel> item = it4.next();
                        GenericLabel numericmana = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        util.setNumericMana(numericmana, p);
                        
                        if(OceManaBar.enabled && oldShow && !OceManaBar.showNumeric)
                        {
                            // we had this label, now we need to remove
                            p.getMainScreen().removeWidget(numericmana);
                            OceManaBar.numericmanas.remove(item);
                        }
                    }

                    sender.sendMessage("OceManaBar Configuration Reloaded.");
                    return true;
                }
            }
            else if (args.length > 0 && args[0].toLowerCase().equals("position"))
            {
                boolean proceed = false;
                if (sender instanceof Player) 
                {
                    if(!(sender.isOp() || sender.hasPermission("ocemanabar.user") || sender.hasPermission("ocemanabar.admin")))
                        sender.sendMessage("You do not have permission to do this.");
                    else
                        proceed = true;
                }
                
                if(proceed)
                {
                    BarOptions tmpOpt = OceManaBar.pMapConfig.get(sender.getName());
                    int tmpX, tmpY;
                    
                    if(args[1].toLowerCase().equalsIgnoreCase("reset"))
                    {
                        tmpX = OceManaBar.posX;
                        tmpY = OceManaBar.posY;
                    }
                    else
                    {
                        tmpX = Integer.parseInt(args[1].toLowerCase());
                        tmpY = Integer.parseInt(args[2].toLowerCase());
                    }
                    
                    tmpOpt.setXpos(tmpX);
                    tmpOpt.setYpos(tmpY);

                    if(OceManaBar.useTexture)
                    {
                        GenericGradient tmpTextureBar = OceManaBar.gradientbars.get(sender);
                        util.SetGradientBar(tmpTextureBar, (SpoutPlayer)sender);
                        
                        GenericGradient tmpBg = OceManaBar.backgrounds.get(sender);
                        util.SetBackgroundBar(tmpBg, (SpoutPlayer)sender);
                    }
                    if(OceManaBar.useAscii)
                    {
                        GenericLabel tmpAsciiBar = OceManaBar.asciibars.get(sender);
                        util.setAsciiBar(tmpAsciiBar, (SpoutPlayer)sender);
                    }
                    if(OceManaBar.showNumeric)
                    {
                        GenericLabel tmpnumericmana = OceManaBar.numericmanas.get(sender);
                        util.setNumericMana(tmpnumericmana, (SpoutPlayer)sender);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}