import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Lunar Lander Game
 * Try to land on the platforms, but slowly!
 * Press Space to thrust, use Left+Right arrow keys to turn 
 * You can't turn while landed, and approaching the platforms too hard or approaching the platforms from the bottom will cause a crash
 * and prompt a restart
 * You also crash when hitting an edge
 * 
 * @author (Hanz Badua) 
 * @version (12 June 2023)
 */
public class LunarLanderWorld extends World
{  
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public LunarLanderWorld()
    {    
        // 1280x720
        super(WIDTH, HEIGHT, 1); 
        Greenfoot.setSpeed(50);
        prepare();
    }
    
    // Initialization
    private void prepare()
    {
        Platform platform = new Platform();
        addObject(platform,1091,613);
        Platform platform2 = new Platform();
        addObject(platform2,668,441);
        Platform platform3 = new Platform();
        addObject(platform3,194,556);
        LunarLander lunarLander = new LunarLander();
        addObject(lunarLander, WIDTH/2, (HEIGHT/8)*2);
    }
}
