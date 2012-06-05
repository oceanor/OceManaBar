package me.oceanor.OceManaBar;

public class BarOptions 
{
    private boolean enabled = true;
    private int x,y;
    
    public BarOptions(int posX, int posY)
    {
        this.x = posX;
        this.y = posY;
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
}
