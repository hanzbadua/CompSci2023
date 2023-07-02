import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class that represents all tower attack actors (even melee effects)
 */
public abstract class Projectile extends Actor
{
    protected double damage = 0;
    
    public Projectile(double d, int rotation) {
        loadProjectileImage();
        setRotation(rotation);
        damage = d;
    }
    
    @Override
    public void act() {
        logic();
    }
    
    protected abstract void logic();
    protected abstract void loadProjectileImage();
}
