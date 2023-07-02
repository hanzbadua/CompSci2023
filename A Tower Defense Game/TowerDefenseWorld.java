import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Proof-of-concept tower defense game
 * Mouse controls only, point-and-click on towers on the tower UI then the canvas
 * to buy+place towers
 * Click towers which are already placed to upgrade them, use the back button to go back to the tower UI
 * 
 * As (mostly) being a proof-of-concept, balance sucks so feel free to mess with the numbers regarding balance (more specifically enemy spawns, values, and tower upgrade values)
 * And there is only one tower due to each tower taking a tremendous amount of time to implement
 * (Projectile behavior, upgrade UI, unique upgrades, specific UI elements, and more..)
 * 
 * Known issue:
 * We use a class, ScheduledExecutorService, for anything time-based
 * This includes everything such as tower attacks every x interval, enemy spawns every x interval, projectile despawns after x time, etc.
 * Sometimes the game will just stop working and execution will continue indefinitely unless you terminate execution yourself
 * I think this is due to a deadlock as a result of multiple queued tasks on one ScheduledExecutorService (?)
 * I think I fixed it by giving each tower its own service, then using a global service for projectiles/enemy spawns
 * If the issue occurs again, try increasing the corePoolSize of each service (the number argument of Executors.newScheduledThreadPool(n), found in this class,
 * and also in Knight.class)
 * 
 * @author (Hanz Badua) 
 * @version (19 June 2023)
 */
public class TowerDefenseWorld extends World
{
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    
    // main game stuff
    private int money = 10;
    private int moneyTransactedOverall = 0;
    private int lives = 100;
    private boolean dead = false;
    private Text moneyDisplay = null;
    private Text livesDisplay = null;
    private Text message = null;
    private Canvas canvas = null;
    private UICanvas ui = null; 
    public ArrayList<UIElement> uiElements = null;
    private HashMap<String, Navnode[]> navnodeCollection = null;
    private ScheduledExecutorService execService = null;
    private ScheduledFuture<?> enemySpawner = null;
    // used when buying towers
    public boolean isBusy = false;
    
    public TowerDefenseWorld()
    {    
        super(1280, 720, 1); 
        Greenfoot.setSpeed(50);
        // set paint order stuff
        setPaintOrder(BuyingTower.class, Tower.class, Enemy.class, TowerRangeHighlighter.class, Projectile.class, Text.class, Canvas.class);
        
        // initialize references for the game canvas base, ui stuff (base, element collection, etc.), executor service (for code which runs on x intervals)
        // also initialize the reference and data for navigation nodes
        canvas = new Canvas();
        ui = new UICanvas();
        moneyDisplay = new Text("$" + money, 32);
        livesDisplay = new Text("Lives: " + lives, 32);
        uiElements = new ArrayList<UIElement>();
        execService = Executors.newScheduledThreadPool(2);
        initializeNavnodeCollections();
        
        // load image as canvas (from currentLevel), then load the canvas and navnode collection into the world
        // also load the ui base into the world
        canvas.loadToWorld(this, navnodeCollection.get("map.png"));
        addObject(ui, 0, 0);
        addObject(moneyDisplay, 1120, 700);
        addObject(livesDisplay, 1120, 600);
        
        enemySpawner = execService.scheduleAtFixedRate(() -> {
            Navnode[] d = navnodeCollection.get("map.png");
            addObject(new PepeD(1 + Math.ceil(moneyTransactedOverall/30)), d[0].getX(), d[0].getY());
        }, 2000, 2000, TimeUnit.MILLISECONDS);
        
        // main menu
        goToMainMenu();
    }
    
    @Override 
    public void act() {
        promptRestart();
    }
    
    // publically expose the executor service reference for towers and projectiles to use (for projectile/attack timings)
    public ScheduledExecutorService getExecService() {
        return execService;
    }
    
    // publically expose current navnode collection for enemies to use
    public Navnode[] getCurrentNavnodeCollection() {
        return navnodeCollection.get("map.png");
    }

    // publically expose money via methods to get/modify
    // required as we also have to update the display, doing it this way ensures that
    public int getMoney() {
        return money;
    }
    
    // remember argument sign matters!
    public void modifyMoney(int m) {
        money += m;
        moneyTransactedOverall += Math.abs(m);
        moneyDisplay.modify("$" + money, 32);
    }
    
    // also expose setter for lives
    public void modifyLives(int l) {
        lives += l;
        livesDisplay.modify("Lives: " + lives, 32);
        
        // dead
        if (lives <= 0) {
            dead = true;
            isBusy = true;
            goToMainMenu();
            
            for (Projectile p: getObjects(Projectile.class)) {
                removeObject(p);
            }
            
            for (Enemy e: getObjects(Enemy.class)) {
                removeObject(e);
            }
            
            for (Tower t: getObjects(Tower.class)) {
                removeObject(t);
            }
            
            enemySpawner.cancel(true);
            
            message("You ran out of lives...\npress SPACE to restart", 24);
        }
    }
    
    // quick restart
    private void promptRestart() {
        if (dead) {
            if (Greenfoot.isKeyDown("space")) {
                Greenfoot.setWorld(new TowerDefenseWorld());
                return;
            }
        }
    }
    
    // initializes navnode collections for each map
    // there is really no nice way to do this other than hardcoding navnode coordinates for each map
    // another way would be to read navnode coordinates from a file and serialize them similar to what I did for Frogger
    // but thats too much work D:
    private void initializeNavnodeCollections() {
        navnodeCollection = new HashMap<String, Navnode[]>();
        
        // for map0.png
        navnodeCollection.put("map.png", new Navnode[] {
            // Starting from the top: navnode going down, going right, going up, going left
            new Navnode(680, 0), new Navnode(670, 388), new Navnode(869, 378), new Navnode(851, 139),
            // going down, going right, going up, going left
            new Navnode(370, 154), new Navnode(370, 490), new Navnode(558, 476), new Navnode(561, 270),
            // going up, going left, going down, going right, ending navnode
            new Navnode(270, 341), new Navnode(261, 70), new Navnode(74, 77), new Navnode(67, 651), new Navnode(959, 620)
        });
    }
    
    // removes all ui elements from the world, and then clears the uielements collection
    public void clearUI() {
        for(UIElement u: uiElements) {
            removeObject(u);
        }
        
        uiElements.clear();
    }
    
    // main menu = menu with all towers and such
    // should be the initial menu
    public void goToMainMenu() {
        clearUI();
        
        Text title = new Text("Towers", 32);
        addObject(title, 1120, 50);
        uiElements.add(title);
        
        KnightElement knight = new KnightElement();
        addObject(knight, 1000, 200);
        uiElements.add(knight);
            
        Text kt1 = new Text("Knight, $10", 22);
        addObject(kt1, 1088, 185);
        uiElements.add(kt1);
        
        Text kt2 = new Text("The conventional melee tower", 22);
        addObject(kt2, 1160, 210);
        uiElements.add(kt2);
    }
    
    // message = text near bottom of ui
    public void message(String value, int size) {
        if (message != null) {
            removeObject(message);
        }
        
        message = new Text(value, size);
        addObject(message, 1120, 670);
        
    }
}
