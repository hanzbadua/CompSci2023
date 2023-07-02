import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * for buying a knight tower
 */
public class BuyingKnight extends BuyingTower
{
    @Override
    protected void setClassImage() {
        setImage(".\\images\\towers\\knight.png");
        GreenfootImage i = getImage();
        i.scale((int)(i.getWidth() * (1.75)), (int)(i.getHeight() * 1.75));
    }
    
    @Override
    protected int getPrice() {
        return 10;
    }
    
    @Override
    protected int getRange() {
        return 100;
    }
    
    @Override
    protected Tower getTower() {
        return new Knight();
    }
}
