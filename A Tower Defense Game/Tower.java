import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.concurrent.*;

/**
 * Base class for a tower, inheriting classes must override methods/super() call properly to provide necessary data 
 * (such as upgrade paths, stat initialization, projectile firing behavior, special effects such as crowd control, and more)
 */
public abstract class Tower extends Actor
{
    public double damage = 0;
    public int range = 0; // range is a radius in pixels!
    public double fireRate = 0; // how many times to fire per second
    protected Tower(double d, int r, int fr) {
        loadTowerImage();
        damage = d;
        range = r;
        fireRate = fr;
    }
    
    @Override
    public void act()
    {
        turnTowardsValidEnemy();
    }
    
    @Override
    public void addedToWorld(World w) {
        registerFiringBehavior();
    }
    
    // turn towards the nearest valid enemy target
    protected void turnTowardsValidEnemy() {
        Enemy e = getClosestValidEnemy();
        if (e != null) 
            turnTowards(e.getX(), e.getY());
    }
    
    // returns the cloest enemy in range, null if no valid enemy in range
    protected Enemy getClosestValidEnemy() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        if (!enemies.isEmpty()) {
            return enemies.get(0);
        } else {
            return null;
        }
    }
    
    protected abstract void loadTowerImage();
    protected abstract void registerFiringBehavior();
}
