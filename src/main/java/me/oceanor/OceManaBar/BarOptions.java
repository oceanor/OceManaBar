package me.oceanor.OceManaBar;

import java.io.Serializable;

public class BarOptions implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean enabled = true;
    private int x,y, height, width;
    
    public BarOptions(int posX, int posY, int height, int width)
    {
        this.x = posX;
        this.y = posY;
        this.height = height;
        this.width = width;
    }
    
    public void setEnabled(boolean value)
    {
        enabled = value;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }

    public void setXpos(int value)
    {
        x = value;
    }

    public int getXpos()
    {
        return x;
    }

    public void setYpos(int value)
    {
        y = value;
    }

    public int getYpos()
    {
        return y;
    }

    public void setWidth(int value)
    {
        width = value;
    }

    public int getWidth()
    {
        return width;
    }
    
    public void setHeight(int value)
    {
        height = value;
    }

    public int getHeight()
    {
        return height;
    }
}
