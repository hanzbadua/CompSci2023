// class that represents the paddle
class Paddle extends Rectangle {
  Paddle() {
    xside = 175; // set width
    yside = 20; // height
    pos.y = 900; // fixed
    // and xposition is dependent on mouse position (see draw())
  }
  
  // paddle color, light blue
  final color paddleColor = color(30, 144, 255);
  
  void draw() {
    // xpos is mousex
    pos.x = mouseX;
    
    fill(paddleColor); // set color
    rect(pos.x, pos.y, xside, yside); // draw paddle
  }
}
