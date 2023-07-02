import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

//
public class LilyPad extends StreamingActor
{
    public LilyPad(int speed) {
        super(speed);
        setImage(".\\images\\LilyPad.png");
        GreenfootImage i = getImage();
        i.scale(i.getWidth() / 2, i.getHeight() / 2);
    }
    
    @Override
    public boolean isGlideable() {
        return true;   
    }
    
    @Override
    public boolean isDangerous() {
        return false;   
    }
}
