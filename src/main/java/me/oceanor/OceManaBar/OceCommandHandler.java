package me.oceanor.OceManaBar;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
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
                    OceManaBar.gradient1 = Utils.strToColor(plugin.getConfig().getString("textureColor1"));
                    OceManaBar.gradient2 = Utils.strToColor(plugin.getConfig().getString("textureColor2"));
                    OceManaBar.bgcolor1 = Utils.strToColor(plugin.getConfig().getString("backgroundColor1"));
                    OceManaBar.bgcolor2 = Utils.strToColor(plugin.getConfig().getString("backgroundColor2"));
                    OceManaBar.segmentChar = plugin.getConfig().getString("segmentChar");
                    OceManaBar.size = plugin.getConfig().getInt("size");

                    if(OceManaBar.manabarType == 2 && OceManaBar.height < 8)
                        OceManaBar.height = 8;
                    if(OceManaBar.manabarType == 2 && OceManaBar.width < 4)
                        OceManaBar.width = 4;
                    
                    Iterator<Entry<Player, GenericLabel>> it1 = OceManaBar.asciibars.entrySet().iterator();
                    while (it1.hasNext())
                    {
                        Entry<Player, GenericLabel> item = it1.next();
                        GenericLabel asciibar = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        p.getMainScreen().removeWidget(asciibar);
                    }
                    Iterator<Entry<Player, GenericGradient>> it2 = OceManaBar.gradientbars.entrySet().iterator();
                    while (it2.hasNext())
                    {
                        Entry<Player, GenericGradient> item = it2.next();
                        GenericGradient bar = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        p.getMainScreen().removeWidget(bar);
                    }
                    Iterator<Entry<Player, GenericGradient>> it3 = OceManaBar.backgrounds.entrySet().iterator();
                    while (it3.hasNext())
                    {
                        Entry<Player, GenericGradient> item = it3.next();
                        GenericGradient bar = item.getValue();
                        SpoutPlayer p = (SpoutPlayer) item.getKey();
                        p.getMainScreen().removeWidget(bar);
                    }
                    OceManaBar.asciibars.clear();
                    OceManaBar.gradientbars.clear();
                    OceManaBar.backgrounds.clear();
                    
                    for(int i = 0; i < OceManaBar.SpoutPlayers.size(); i++)
                    {
                        String pname = OceManaBar.SpoutPlayers.get(i);
                        SpoutPlayer plr = (SpoutPlayer) Bukkit.getPlayer(pname);
                        
                        if(OceManaBar.enabled && plr.hasPermission("ocemanabar.show"))
                        {   
                            if(OceManaBar.useTexture)
                            {
                                util.SetGradientBar(new GenericGradient(), plr);
                                util.SetBackgroundBar(new GenericGradient(), plr);
                            }

                            if(OceManaBar.useAscii)
                            {
                                util.setAsciiBar(new GenericLabel(), plr);
                            }
                            
                            if(OceManaBar.showNumeric)
                            {
                                util.setNumericMana(new GenericLabel(), plr);
                            }
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
            else if (args.length > 0 && args[0].toLowerCase().equals("size"))
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
                    int tmpWidth, tmpHeight;
                    
                    if(args[1].toLowerCase().equalsIgnoreCase("reset"))
                    {
                        tmpWidth = OceManaBar.width;
                        tmpHeight = OceManaBar.height;
                    }
                    else
                    {
                        tmpWidth = Integer.parseInt(args[1].toLowerCase());
                        tmpHeight = Integer.parseInt(args[2].toLowerCase());
                    }
                    
                    tmpOpt.setWidth(tmpWidth);
                    tmpOpt.setHeight(tmpHeight);

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