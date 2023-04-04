// class that represents the ball
class Ball {
  boolean held = true; 
  boolean isSuperFast = false; // if ball x is super fast reduce the y speed to compensate
  // white
  final color ballColor = color(255);
  
  // circle
  int diameter = 20;
  
  // stores position
  PVector pos = new PVector(); 
  
  // stores velocity
  PVector vel = new PVector(0, 8);
  
  Ball() {
    // default ball positions
    pos.x = 600;
    pos.y = 600;
  }
  
  void draw() {
    fill(ballColor);
    
    heldLogic();
    collision();
    
    circle(pos.x, pos.y, diameter);
    pos.add(vel); // movement based on velocity
  }
  
  // handles all collision + bouncing physics, checks and logic, etc.
  void collision() {
    if (held) return; // don't handle collision if the ball is held
    
    if (pos.x <= diameter || pos.x >= width - diameter) // left/right edge 
      vel.x *= -1; // flip
    else if (pos.y <= diameter) { // upper edge
      vel.y *= -1;
      pos.y += ball.vel.y > 0 ? 10 : -10;
    }
    else if (pos.y >= height - diameter) { // bottom edge, fail
      if (lives > 0) {
        lives--;
        ball = new Ball();
      } else {
        onGameOver = true;
      }
    }
    
    // is colliding with paddle
    else if (ballCollidesWithRect(this, paddle)) { // collides with paddle
      // depending on the score, increment y speed every few score intervals
      countdownToNextYIncrement--;
      if (countdownToNextYIncrement == 0) {
        countdownToNextYIncrement = countdownToNextCountdown;
        countdownToNextCountdown += 2;
        // remember to respect direction
        vel.y += vel.y >= 0 ? 1 : -1;
      }
    
      // handle x speed based on collision position relative to the center of the paddle and some normalization stuff after
      vel.x = (((pos.x - paddle.pos.x) * 1.25) - sqrt(abs((pos.x - paddle.pos.x) * 2))) / 8;
      
      if (isSuperFast == false && abs(vel.x) > 7) {
        vel.y -= vel.y >= 0 ? 3 : -3;
        isSuperFast = true;
      } else if (isSuperFast == true && abs(vel.x) <= 7) {
        vel.y += vel.y >= 0 ? 3 : -3;
        isSuperFast = false;
      }
      
      // flip y
      vel.y *= -1;  
      
      // slight adjustment to prevent collision issues
      pos.y += ball.vel.y > 0 ? 10 : -10;
    }
  }
  
  // pin ball to paddle until lmb if held
  void heldLogic() {
    if (held) {
      pos.x = paddle.pos.x;
      pos.y = paddle.pos.y - paddle.yside;
      
      if (mousePressed == true)
        held = false;
    }
  }
}
