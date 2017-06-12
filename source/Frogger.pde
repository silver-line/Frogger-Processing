Frog frog;
RowController rControl;
RowController rControl1;
Finish fLine;
int frogrow;
PImage frogpic;
float grid = 25;
boolean ismoving = false;
int highScore = 0;
int currentScore = 0;

void setup(){
  size(575,800);
  frogpic = loadImage("frog.png");
  frogpic.resize(25,25);
  
  frog = new Frog(width/2-grid/2,height-grid,grid);
  frogrow  = 1;
  rControl = new RowController(true);
  fLine = new Finish();
  
  //Run for a sec to get cars in places
  for(int i=0;i<1200;i++){
    rControl.update();
  }
  frameRate(60);
}

void draw(){
  background(50);
  fLine.show();
  rControl.update();
  rControl.show(); 
  frog.show(); 
  if(ismoving){
    rControl1.show();
    rControl1.update();
    frog.moveDown();
    if(rControl.hasmoved >= height -25){
      rControl1.stopMove();
      rControl = rControl1;
      rControl1 = null;
      frameRate(60);
      ResetFrog(false);
      ismoving = false;
    }
  }else if(frogrow > 29){
    ismoving = true;
    rControl.moveDown();
    frameRate(120);
    rControl1 = new RowController(false);
      for(int i=0;i<1200;i++){
        rControl1.update();
      }
    rControl1.moveDown();
  }
  fill(200);
  rect(0,0,125,75);
  fill(0);
  text("HighScore: " + highScore,10,15);
  text("CurrentScore: " + currentScore,10,30);
}


void keyPressed(){
  if(!ismoving){
    if(keyCode == UP){
      frog.move(0,1);
      frogrow++;
      currentScore++;
      if(currentScore > highScore){
        highScore = currentScore;
      }
      rControl.setFrogRow();
    }else if(keyCode == DOWN){
      if(frogrow > 1){
        frogrow--;
        currentScore--;
        rControl.setFrogRow();
      }
      frog.move(0,-1);
    }else if(keyCode == LEFT){
      frog.move(-1,0);
    }else if(keyCode == RIGHT){
      frog.move(1,0);
    }
  }
}

void ResetFrog(boolean died){
  float tleft;
  float tright;
  if(died){
    currentScore = 0;
    frog = new Frog(width/2-grid/2,height-grid,grid);
  }else{
    tleft = frog.left;
    tright = frog.right;
    frog = new Frog(width/2-grid/2,height-grid,grid);
    frog.left = tleft;
    frog.right = tright;
  }
  frogrow = 1;
  rControl.setFrogRow();
}