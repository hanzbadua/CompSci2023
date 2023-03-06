import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// Class which extends the World class and is the "main menu" portion of the game
// This is the world that should be instantiated first/the intended starting world of the game
// Although this world is where the game starts/initializes,
// the header comment is located in SpaceWorld.java (the world responsible for the actual game)
public class MainMenuWorld extends World
{
    // stored difficulty - default is normal
    private Difficulty difficulty = Difficulty.NORMAL;
    
    // main menu buttons
    private Button startGameButton;
    private Button changeDifficultyButton;
    
    // Constructor for the MainMenuWorld class
    public MainMenuWorld()
    {    
        super(SpaceWorld.WIDTH, SpaceWorld.HEIGHT, 1);
        
        // force the game speed to be normal in case its changed (range is 0...100 so 50 is normal)
        Greenfoot.setSpeed(50);
        
        // game title text O_O ... 
        showText("A Pursuit Game In Space", 300, 50);
        
        // instantiate buttons with position arguments
        startGameButton = new Button(300, 100);
        changeDifficultyButton = new Button(300, 150);
        
        // add buttons to world, see method below
        addButtonToWorld(startGameButton, changeDifficultyButton);
        
        // set button text
        startGameButton.displayText("Start game");
        
        // default difficulty is normal
        changeDifficultyButton.displayText("Difficulty: normal");
        
        // display instructions text
        showText("Goal: collect powerups to grow+become faster", 300, 200);
        showText("Avoid the enemies; colliding = lose life", 300, 225);
        showText("Running out of lives = lose game", 300, 250);
        showText("Collecting all required powerups unlocks the laser weapon", 300, 275);
        showText("Eliminate all enemies with the laser = win game", 300, 300);
        showText("Controls: Left/right arrow keys to turn left/right", 300, 325);
        showText("Hold D key to decelerate to minimum speed", 300, 350);
        showText("And press the F key to fire your laser (when unlocked)", 300, 375);
    }
    
    // called every frame, defines button behavior
    public void act() {
        // Check if the button is being clicked
        if (Greenfoot.mouseClicked(startGameButton)) {
            // if start game button is clicked, start the game by instantiating SpaceWorld (the world representing the actual game)
            // and switch to it
            Greenfoot.setWorld(new SpaceWorld(difficulty));
        } else if (Greenfoot.mouseClicked(changeDifficultyButton)) // difficulty button
        {
            // cycle through the different difficulties
            switch(difficulty) {
                case EASY: // if diff == easy cycle to normal
                    difficulty = Difficulty.NORMAL;
                    changeDifficultyButton.displayText("Difficulty: normal");
                    break;
                case NORMAL: // normal --> hard
                    difficulty = Difficulty.HARD;
                    changeDifficultyButton.displayText("Difficulty: hard");
                    break;
                case HARD: // hard --> loop back to easy
                    difficulty = Difficulty.EASY;
                    changeDifficultyButton.displayText("Difficulty: easy");
                    break;
            }
        }
    }
    
    // function to make adding buttons to a world a bit easier (so we don't write x/y properties out over and over again)
    // the ... operator indicates a varargs parameter in java, aka a list of stuff of the appropriate type that you can pass
    // so this function can be called as so: addButtonToWorld(button1, button2, button3) without needing to pass a list as an argument
    // and doing all that list work in the function itself
    private void addButtonToWorld(Button... add) { // Button... = Button array
        for (Button i: add) // iterate through every individual Button object ("i") in the Button array ("add")
            // and add them to the world with the expected x and y values
            addObject(i, i.getExpectantX(), i.getExpectantY());
    }
}
