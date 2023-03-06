// class that represents the ball
class Ball {
  // white
  final color ballColor = color(255);
  
  // ball diameter
  final static int DIAMETER = 40;
  
  // stores ball postions
  int x = 1000;
  int y = 500;

  // speed
  int xspeed = 0;
  int yspeed = 10;
  
  // to prevent faulty repeated collision
  int iframes = IFRAMES_ON_RESET;
  final static int IFRAMES_ON_RESET = 3; // 3 iframes means a twentieth of a second at 60fps
  
  void draw() {
    fill(ballColor);
    
    edgeBounce();
    
    pushMatrix();
    translate(x, y);
    circle(0, 0, DIAMETER);
    x += xspeed;
    y += yspeed;
    popMatrix();
    
    // decrement iframes every frame, but don't go negative
    if (iframes > 0)
      iframes--;
  }
  
  // only bounce if iframes isn't 0
  void edgeBounce() {
    if (iframes > 0) 
      return;
    
    if (x <= 1 || x >= width - 1) // left/right edge 
    {
      xspeed *= -1; // flip
    }
    
    if (y <= 1 || y >= height - 1) // up/down edge
    {
      yspeed *= -1;
    }
    
    if (isCollidingWithPaddle())
      yspeed *= -1;
      
    // reset iframes
    iframes = IFRAMES_ON_RESET;
  }
  
  // checks if the ball is colliding with the paddle
  // taken from https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
  // because geometry is too much work
  // "paddle" refers to the game Paddle reference defined in the main file (BreakoutGame_processing.pde)
  boolean isCollidingWithPaddle() {
    // remember that the paddle x position is mouseX
    // cx/cy = ball distance x/y from paddle
    int cx = abs(x - mouseX);
    int cy = abs(y - Paddle.YPOS);
    int r = DIAMETER / 2; // ball radius
    
    if (cx > (paddle.width/2 + r)) 
      return false;
      
    if (cy > (Paddle.HEIGHT/2 + r))
      return false;
      
    if (cx <= (paddle.width/2))
      return true;
      
    if (cy <= (Paddle.HEIGHT/2))
      return true;
      
    int cornerDistance = (cx - (paddle.width/2))^2 + (cy - (Paddle.HEIGHT/2))^2;
    
    return cornerDistance <= (r^2);
  }
}
