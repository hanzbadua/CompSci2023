import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * UI element which represents a tower that can be bought
 */
public abstract class TowerElement extends UIElement
{
    public TowerElement() {
        setElementImage();
    }
    
    @Override
    public void act()
    {
        onClick();
    }
    
    protected abstract void onClick();
    protected abstract void setElementImage();
}
