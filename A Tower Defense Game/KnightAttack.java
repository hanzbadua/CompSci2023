import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.concurrent.*;

/**
 * Projectile which represents knight melee attack
 */
public class KnightAttack extends Projectile
{
    // used for making the projectile disappear after
    private ScheduledFuture<?> lifespan;
    // how long melee attack should be alive for
    // should be same time as knight fire rate
    private double lifespanTime = 0;
    // if in cosmetic mode, no longer deals damage but the effect is still ongoing
    // this is so the player still sees the effect knowing the knight tower did something, but now the effect wont repeatedly damage stuff
    // that it isnt supposed to
    // if that makes sense
    private boolean cosmeticMode = false;
    
    public KnightAttack(double d, int r, double lt) {
        super(d, r);
        lifespanTime = lt;
    }
    
    @Override
    protected void logic() {
        if (cosmeticMode) 
            return;
            
        Enemy e = (Enemy) getOneIntersectingObject(Enemy.class);
        if (e != null) {
            e.modifyHealth(-damage);
            cosmeticMode = true;
        }
    }
    
    @Override 
    protected void loadProjectileImage() {
        setImage(".\\images\\projectiles\\KnightAttack.png");
    }
    
    @Override 
    public void addedToWorld(World w) { 
        lifespan = ((TowerDefenseWorld)w).getExecService().scheduleAtFixedRate(() -> {
            lifespan.cancel(false);
            w.removeObject(this);
            return;
        }, 100, (long)lifespanTime, TimeUnit.MILLISECONDS);
    }
}
