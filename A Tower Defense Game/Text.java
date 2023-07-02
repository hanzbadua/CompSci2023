import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Small actor to help with handling text
 * much better than using showText() imo
 */
public class Text extends UIElement
{   
    public Text(String value, int size) {
        setImage(new GreenfootImage(value, size, null, null));
    }
    
    // cheaper than reinstantiating
    public void modify(String value, int size) {
        setImage(new GreenfootImage(value, size, null, null));
    }
}
