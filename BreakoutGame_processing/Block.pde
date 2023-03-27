class Block extends Rectangle {
  BlockPowerup powerup = BlockPowerup.PADDLESIZE;
  BlockStrength strength = BlockStrength.ONE;
  
  // constructor which requires initial strength, and specifies width/height dimensions
  Block(BlockStrength strength) {
    xside = 145;
    yside = 30;
    this.strength = strength;
  }
  
  void draw() {
    fill(getBlockStrengthColor(strength));
    rect(pos.x, pos.y, xside, yside);
    //drawPowerupShape(this);
  }
}
