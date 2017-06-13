//Frogger Game

//Main Game Controller
RowController rControl;
//Second Game Controller to temporarily hold next
//level while animating between them.
RowController rControl1;

//Main frog object
Frog frog;
//Finish Line Object 
Finish fLine;


//Various Global Variables
float grid = 25;
int frogRow = 0;
int highScore = 0;
int currentScore = 0;
//Keep track of if we are changing levels or not and turn on and off game logic accordingly
boolean ismoving = false;

//Transparent Frog PNG
PImage frogpic;

void setup(){
  size(575,800);
  frogpic = loadImage("frog.png");
  frogpic.resize(25,25);
  
  frog = new Frog(width/2-grid/2,height-grid,grid);
  frogRow  = 1;
  rControl = new RowController(true);
  fLine = new Finish();
  
  //Only adjusting this so that I can ramp up durring level change. Otherwise objects didnt line up.
  frameRate(60);
}

void draw(){
  background(50);
  fLine.show();
  rControl.update();
  rControl.show(); 
  frog.show(); 

  //Run if currently changing levels
  if(ismoving){
    rControl1.show();
    rControl1.update();
    frog.moveDown();
    
    //Stop moving once the original level is offscreen and the new level is exatly in place.
       //Switch out the new level in place of the old row controller. Clear the temporary controller.
    if(rControl.hasmoved >= height -25){
      rControl1.stopMove();
      rControl = rControl1;
      rControl1 = null;
      frameRate(60);
      ResetFrog(false);
      ismoving = false;
    }
  //If the frog is in row 30 or higher (Finsh line row) start process to change levels
  }else if(frogRow > 29){
    ismoving = true;
    rControl.moveDown();
    rControl1 = new RowController(false);
    rControl1.moveDown();
    frameRate(120);
  }
  
  //Score Panel in the upper left
  fill(200);
  rect(0,0,125,75);
  fill(0);
  text("HighScore: " + highScore,10,15);
  text("CurrentScore: " + currentScore,10,30);
}


void keyPressed(){
  //Only respond to key presses if we are not currently changing levels
  if(!ismoving){
    
    //On key up,move frog, increase the current score, and update what row the frog is currently in for calculations.
    if(keyCode == UP){
      currentScore++;
      if(currentScore > highScore){
        highScore = currentScore;
      }
      frog.move(0,1);
      frogRow++;
      rControl.setfrogRow();
      
    //On key down,move frog, decrease the current score, and update what row the frog is currently in for calculations.
    }else if(keyCode == DOWN){
      if(frogRow > 1){
        currentScore--;
        frogRow--;
        rControl.setfrogRow();
      }
      frog.move(0,-1);
    }else if(keyCode == LEFT){
      frog.move(-1,0);
    }else if(keyCode == RIGHT){
      frog.move(1,0);
    }
  }
}

//Reset the frog to the bottom row, only set him in the middle and reset score if you've died.
void ResetFrog(boolean died){
  if(died){
    currentScore = 0;
    frog = new Frog(width/2-grid/2,height-grid,grid);
  }else{
    frog = new Frog(frog.left,height-grid,grid);
  }
  
  //Set that the frog is in row one, and tell the row controller of this change.
  frogRow = 1;
  rControl.setfrogRow();
}