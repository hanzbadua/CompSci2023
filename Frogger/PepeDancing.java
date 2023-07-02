import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.concurrent.*;

/**
 * The best Alligator substitute there is
 */
public class PepeDancing extends StreamingActor
{
    // image cache, to prevent loading image upon each swap
    private GreenfootImage passiveImage = null;
    private GreenfootImage hostileImage = null;
    
    private boolean _isDangerous = false;
    
    // swap between not dangerous and dangerous every few seconds
    private ScheduledFuture<?> timer;
    
    public PepeDancing(int speed) {
        super(speed);
        
        // initialize image cache, then scale properly
        passiveImage = new GreenfootImage(".\\images\\peped.png");
        passiveImage.scale(passiveImage.getWidth() / 4, passiveImage.getHeight() / 4);
        
        hostileImage = new GreenfootImage(".\\images\\peped_mad.png");
        hostileImage.scale(hostileImage.getWidth() / 4, hostileImage.getHeight() / 4);
        
        setImage(passiveImage);
    }
    
    @Override
    protected void addedToWorld(World _w) {
        // should always work
        FroggerWorld w = (FroggerWorld) _w;
        
        // flip danger state every x random ms interval
        timer = w.scheduledExec.scheduleAtFixedRate(() -> {
            // flip to become dangerous
            if (_isDangerous == false) {
                _isDangerous = true;
                setImage(hostileImage);
            } else { // flip to become passive
                _isDangerous = false;
                setImage(passiveImage);
            }
        }, 0, Greenfoot.getRandomNumber(300) + 1200, TimeUnit.MILLISECONDS);
    }
    
    // stops the timer used to swap state every ms interval, call this before removing from world!
    public void stopTimer() {
        timer.cancel(false);
    }
    
    @Override
    public boolean isGlideable() {
        return true;   
    }
    
    @Override
    public boolean isDangerous() {
        return _isDangerous;
    }
    
    @Override 
    protected boolean checkForDestruction() {
        if (super.checkForDestruction()) {
            stopTimer();
            return true;
        }
        
        return false;
    }
}
