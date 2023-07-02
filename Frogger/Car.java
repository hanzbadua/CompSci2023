import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//
public class Car extends StreamingActor
{
    public Car(int speed) {
        super(speed);
        // we only have one car image right now, add more later
        setImage(".\\images\\YellowCar.png");
        GreenfootImage i = getImage();
        
        // scale
        i.scale(i.getWidth() * 2, i.getHeight() * 2);
        
        // rotate properly based on positive/negative speed
        if (speed < 0)
            setRotation(270);
        else
            setRotation(90);
    }
    
    @Override
    public boolean isGlideable() {
        return false;   
    }
    
    @Override
    public boolean isDangerous() {
        return true;   
    }
}
