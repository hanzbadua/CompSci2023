import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Used to buy Knight towers
 */
public class KnightElement extends TowerElement
{
    @Override
    public void onClick() {
        // should not be null
        TowerDefenseWorld w = getWorldOfType(TowerDefenseWorld.class);
        if (Greenfoot.mouseClicked(this) && w.isBusy == false) {
            w.isBusy = true;
            MouseInfo m = Greenfoot.getMouseInfo(); // this shouldn't be null at this time!
            w.addObject(new BuyingKnight(), m.getX(), m.getY());
        }
    }
    
    @Override
    public void setElementImage() {
        setImage(".\\images\\ui\\knightUI.png");
        GreenfootImage i = getImage();
        i.scale((int)(i.getWidth() * 1.75), (int)(i.getHeight() * 1.75));
    }
}
