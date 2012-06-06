package me.oceanor.OceManaBar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Utils
{
    public OceManaBar plg;
    
    public Utils(OceManaBar plugin)
    {
        this.plg = plugin;
    }
    
    public void savePlayerConfigs(Map<String, BarOptions> pMapConfig, String filename) throws FileNotFoundException, IOException
    {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(pMapConfig);
        oos.close();
    }
    
    public HashMap<String, BarOptions> loadPlayerConfigs(String filename) throws IOException, FileNotFoundException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        @SuppressWarnings("unchecked")
        HashMap<String, BarOptions> tmp = (HashMap<String, BarOptions>) ois.readObject();
        ois.close();
        return tmp;
    }
    
    public void SetGradientBar(GenericGradient gradientBar, SpoutPlayer plr)
    {
        BarOptions playerOptions = OceManaBar.pMapConfig.get(plr.getName());
        
        // gradient bar
        gradientBar.setX(playerOptions.getXpos() +1).setY(playerOptions.getYpos() +2).setWidth(OceManaBar.width -3).setHeight(OceManaBar.height - 7);
        gradientBar.setBottomColor(OceManaBar.gradient1).setTopColor(OceManaBar.gradient2).setPriority(RenderPriority.Lowest);

        plr.sendMessage("Colore 1: " + OceManaBar.c1 + " Colore 2: " + OceManaBar.c2);
        
        plr.getMainScreen().attachWidget(plg, gradientBar);
        OceManaBar.gradientbars.put(plr,gradientBar);
    }

    public void SetBackgroundBar(GenericGradient bg, SpoutPlayer plr)
    {
        BarOptions playerOptions = OceManaBar.pMapConfig.get(plr.getName());

        // black background / frame
        bg.setX(playerOptions.getXpos()).setY(playerOptions.getYpos()).setWidth(OceManaBar.width).setHeight(OceManaBar.height -3);
        bg.setBottomColor(OceManaBar.bgcolor1).setTopColor(OceManaBar.bgcolor2).setPriority(RenderPriority.Highest);

        plr.getMainScreen().attachWidget(plg, bg);
        OceManaBar.backgrounds.put(plr,bg);
    }
    
    public void setAsciiBar(GenericLabel textBar, SpoutPlayer plr)
    {
        BarOptions playerOptions = OceManaBar.pMapConfig.get(plr.getName());

        textBar.setAuto(false).setX(playerOptions.getXpos()).setY(playerOptions.getYpos()).setWidth(OceManaBar.width).setHeight(OceManaBar.height);
        
        String textbar = "";
        textbar += ChatColor.DARK_GRAY + "[" + ChatColor.BLUE;
        int i;
        for (i=0 ; i<OceManaBar.size; i++)
            textbar += OceManaBar.segmentChar;
        textbar += ChatColor.DARK_GRAY + "]";
        textBar.setText(textbar);
        
        plr.getMainScreen().attachWidget(plg, textBar);
        OceManaBar.asciibars.put(plr,textBar);
    }
    
    public void setNumericMana(GenericLabel numBar, SpoutPlayer plr)
    {
        BarOptions playerOptions = OceManaBar.pMapConfig.get(plr.getName());
        
        numBar.setAuto(false).setX(playerOptions.getXpos() + OceManaBar.width).setY(playerOptions.getYpos()).setWidth(35).setHeight(OceManaBar.height);
        numBar.setText("[" + OceManaBar.maxMana + "/" + OceManaBar.maxMana + "]");
        plr.getMainScreen().attachWidget(plg, numBar);
        OceManaBar.numericmanas.put(plr,numBar);
    }

    public static Color strToColor(String strcolor)
    {
        java.awt.Color jcolor = null;
        try
        {
            Field field = Class.forName("java.awt.Color").getField(strcolor.toLowerCase());
            jcolor = (java.awt.Color)field.get(null);
        }
        catch (Exception e)
        {
            jcolor = java.awt.Color.BLACK;
        }

        return new Color(jcolor.getRed(),jcolor.getGreen(),jcolor.getBlue(),jcolor.getAlpha());
    }
}
