/*
 * Breakout game port to Processing  
 */

// main game stuff
Paddle paddle = new Paddle();
Ball ball = new Ball();

// initialize stuff
void setup() {
  // p2d is faster than default 2d renderer (java2d)
  // fullScreen = set width/height to whole screen = assume 1920 * 1080
  fullScreen(P2D);
  
  // ensure framerate is 60fps
  frameRate(60);
  
  // in Processing by default, the x/y values of a rectangle represent the top-left corner of a rectangle
  // using the function rectMode() with the argument CENTER changes the behavior so that
  // the x/y values represent the center of a rectangle (similar to Greenfoot)
  rectMode(CENTER);
}

void draw() {
  background(0); // black background
  
  paddle.draw();
  ball.draw();
}
