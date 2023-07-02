import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * main game enemy
 */
public class PepeD extends Enemy
{
    public PepeD(double hp) {
        super(hp, (int)(1+Math.floor(hp/4)), (int)hp);
    }
    
    @Override
    public void loadEnemyImage() {
        if (hp <= 2) 
            setImage(".\\enemies\\pepedy.png");
        else if (hp > 2 && hp <= 5)
            setImage(".\\enemies\\pepedy.png");
        else
            setImage(".\\enemies\\pepedo.png");
            
        GreenfootImage i = getImage();
        i.scale(i.getWidth()/6, i.getHeight()/6);
    }
}
