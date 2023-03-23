class Block extends Rectangle {
  BlockPowerup powerup;
  BlockStrength strength = BlockStrength.ONE;
  
  // constructor which requires initial strength, and specifies width/height dimensions
  Block(BlockStrength strength) {
    xside = 125;
    yside = 30;
    this.strength = strength;
  }
  
  void draw() {
    fill(getBlockStrengthColor(strength));
    rect(pos.x, pos.y, xside, yside);
    collision();
  }
  
  void collision() {
    if (ballCollidesWithRect(ball, this))
    {
      strength = strength.downgrade();
      if (strength == null)
        blocks.remove(this);
    }
  }
}
