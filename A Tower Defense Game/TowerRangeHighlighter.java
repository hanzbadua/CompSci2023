import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Visually represents a towers range when buying it  
 * Will also be: blue if valid to place, red if not
 */
public class TowerRangeHighlighter extends Actor
{
    private int range = 0;
    
    public TowerRangeHighlighter(int range) {
        this.range = range;
        toInvalidState();
    }
    
    public void toValidState() {
        GreenfootImage i = new GreenfootImage(range, range);
        i.setColor(new Color(0, 0, 255));
        i.drawOval(0, 0, range, range);
        i.fillOval(0, 0, range, range);
        i.setTransparency(128);
        setImage(i); 
    }
    
    public void toInvalidState() {
        GreenfootImage i = new GreenfootImage(range, range);
        i.setColor(new Color(255, 0, 0));
        i.drawOval(0, 0, range, range);
        i.fillOval(0, 0, range, range);
        i.setTransparency(128);
        setImage(i); 
    
    }
    
    public void hide() {
        setImage(new GreenfootImage(1, 1));
    }
}
