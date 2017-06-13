import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Frogger extends PApplet {

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

public void setup(){
  
  frogpic = loadImage("frog.png");
  frogpic.resize(25,25);
  
  frog = new Frog(width/2-grid/2,height-grid,grid);
  frogRow  = 1;
  rControl = new RowController(true);
  fLine = new Finish();
  
  //Only adjusting this so that I can ramp up durring level change. Otherwise objects didnt line up.
  frameRate(60);
}

public void draw(){
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


public void keyPressed(){
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
public void ResetFrog(boolean died){
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
//All Car or Log Logic is done via the containing Row.
class Car extends Rectangle{
  float xspeed;
  int r;
  int g;
  int b;
  
  Car(float x,float y,float w, float h,float xspeed,int r,int g, int b){
    super(x,y,w,h);
    this.xspeed = xspeed;
    
    //Set a cars color to the exact opposite of the containing rows color for optimal visability.
    this.r = 255-r;
    this.g = 255-g;
    this.b = 255-b;
  }
  
  
  public void show(){
    fill(r,g,b);
    rectMode(CORNERS);
    if(xspeed>0){
    //Car is drving Right (Display front of truck on right)
       rect(this.left,this.bottom - 3,this.right-7,this.top + 3);
       fill(r+10,g+10,b+10);
       rect(this.right-7,this.bottom - 3.5f,this.right,this.top + 4.5f);
    }else{
    //Car is drving Left (Display front of truck on left)
       rect(this.left+7,this.bottom - 3,this.right,this.top + 3);
       fill(r+10,g+10,b+10);
       rect(this.left,this.bottom - 3.5f,this.left+7,this.top + 4.5f);
    }
  }
  
  public void update(){
    this.cpos(xspeed,0);
  }
  
  public void moveDown(){
    this.cpos(0,-5);
  }
}
class Finish{
  
  Finish(){
    
  }
  
  //Draw Checkerboard pattern
  public void show(){
    stroke(1);
    fill(255);
    for(int j = 0;j<3;j++){
      for(int i=0;i<23;i++){
        if(i % 2 == 0 && j % 2 == 0){
          rect(i*grid,grid*j,i*grid + grid,grid + (grid*j));
        }else if(i % 2 == 1 && j % 2 == 1){
          rect(i*grid,grid*j,i*grid + grid,grid + (grid*j));
        }
      }
    }
  }
}
class Frog extends Rectangle{
  
  Frog(float x,float y,float w){
    super(x,y,w,w);
  }
  
  public void show(){
    image(frogpic,this.left,this.top);
  }
  
  public void move(float x, float y){
   cpos(x*grid,y*grid); 
   this.keepOnMap();
  }
  
  public void moveDown(){
    this.cpos(0,-5);
  }
  
  //Helper function to let the log row move the frog with it confined to the screen.
  public void logMove(float move){
    this.cpos(move,0);
    this.keepOnMap();
  }
}
//Basicly just a car with diffrent shading
//All Car or Log Logic is done via the containing Row.
class Log extends Car{

  Log(float x,float y,float w, float h,float xspeed){
    super(x,y,w,h,xspeed,0,0,0);
    
    //Set the logs color to a random brown ish color.
    float rgb = random(1);
    this.r = PApplet.parseInt(map(rgb,0,1,75,183));
    this.g = PApplet.parseInt(map(rgb,0,1,54,133));
    this.b = PApplet.parseInt(map(rgb,0,1,9,26));
  }
  
  public void show(){
    stroke(1);
    fill(r,g,b);
    rectMode(CORNERS);
    rect(this.left,this.bottom - 3,this.right,this.top + 3);
    noStroke();
    fill(r+10,g+10,b+10);
    rect(right-7,top+4,right-1,bottom-3);
    rect(left+7,top+4,left+1,bottom-3);
  }
}
class Rectangle{
  float left;
  float right;
  float top;
  float bottom;
  float w;
  float h;
  
  Rectangle(float x,float y,float w, float h){
    left = x;
    right = x + w;
    top = y;
    bottom = y + h;
    this.w = w;
    this.h = h;
  }
  
  
  //Internal Helper function to make moving rectangles easier
  public void cpos(float x, float y){
    if(y==0){
      //X moved, calc new left & right
      left = left + x;
      right = right + x;
    }else{
      //Y moved, calc new top & bottom
      top = top - y;
      bottom = bottom - y;
    }  
  }
  
  //Check if rect is out of bounds
  public boolean isOffMap(){
    if(left>=width){
     return true;
    }else if(right<=0){
      return true;
    }else{
     return false; 
    }
  }
  
  
  //Both a check if out of bounds, and an automatic move back onto map if true.
    //Called everytime the frog is moved, this includes user movement, and if the frog is sitting on a log.
  public void keepOnMap(){
    if(left>=width - w){
     left = width - w;
     right = width;
    }else if(right<=w){
      left = 0;
      right = w;
    }
    if(top>=height){
      top = height-h;
      bottom = height;
    }else if(bottom<=0){
      top = 0;
      bottom = h;
    }
  }
  
}
//Most running game logic is contained in the rows.
//We let the row know if the frog is in its row, and if so the row takes care of everything.

class Row{
  float chance;
  float y;
  float w;
  float h;
  float xspeed;
  float cooldown;
  float ccd;
  int r;
  int g;
  int b;
  int rowNum;
  boolean froginrow = false;
  boolean logs;
  ArrayList<Car> cars = new ArrayList();
  
  Row(float chance,float y,float w,float h,float xspeed, boolean logs){
    this.chance = chance;
    this.rowNum = PApplet.parseInt(y);
    this.y = height - (y * grid);
    this.xspeed = xspeed;
    this.w = w;
    this.h = h;
    
    //Set the minimum cooldown time between when the next log or car can be spawned in the row.
      //Logs have a much higher cooldown to prevent too many logs at a time.
    if(logs){
      this.cooldown = (w*5*map(abs(xspeed),0.75f,3,0.75f,1));
    }else{
      this.cooldown = 10 + grid*3;
    }
    this.logs = logs;
    ccd = this.cooldown;
    
      //Set Row color
    if(logs){
      //If log row, set random shade of blue
      float rgb = random(1);
      r= PApplet.parseInt(map(rgb,0,1,28,48));
      g= PApplet.parseInt(map(rgb,0,1,60,103));
      b= PApplet.parseInt(map(rgb,0,1,144,255));
    }else{
      //If car row set any random color
      r= PApplet.parseInt(random(0,255));
      g= PApplet.parseInt(random(0,255));
      b= PApplet.parseInt(random(0,255));
    }
  }
  
  public void addCar(){
    //Add a car or log, on either the right or left side depending on xspeed direction.
    if(xspeed > 0){
      if(logs){
        cars.add(0,new Log(0-w,y,w,h,xspeed));
      }else{
        cars.add(0,new Car(0-w,y,w,h,xspeed,r,g,b));
      }
    }else{
      if(logs){
        cars.add(0,new Log(width,y,w,h,xspeed));
      }else{
        cars.add(0,new Car(width,y,w,h,xspeed,r,g,b));
      }
    }
  }
  
  public void update(){
    testAddNew();
    ArrayList<Car> toRemove = new ArrayList();
    for(Car c: cars){
      c.update();
      if(c.isOffMap()){
        toRemove.add(c);
      }
    }
    cars.removeAll(toRemove);
   hasTouched();
  }
  
  //Helper function to randomly spawn a car or log depending on both the chance variable that is randomly set per row,
    //as well as if the cooldown has been reached.
  public void testAddNew(){
    if(ccd > cooldown){
      if(random(1)<chance){
        addCar();
        ccd = 0;
        
      //if we havent spawned a car or log in over 3 times the cooldown, just spawn one.
      }else if(ccd > cooldown * 3){
        addCar();
        ccd = 0;
      }else{
        ccd++;
      }
    }else{
      ccd++;
    }
  }
  
  public void moveDown(){
    for(Car c: cars){
      c.moveDown();
    }
    y = y + 5;
  }
  
  public void show(){
    //If the frog is currently in this row, we highlight it slightly to make things easier to see.
    if(froginrow){
      if(logs){
        noStroke();
        fill(r+50,g+50,b+50);
      }else{
        stroke(1);
        fill(200,200,200);
      }
    }else{
      stroke(1);
      if(logs){
        noStroke();
      }
      fill(r,g,b);
    }
    rect(0,y,width,y + h);
    for(Car c: cars){
      c.show();
    }
  }
  
  //Collision Logic
  public void hasTouched(){
    if(froginrow){
      if(logs){
        //Logs Logic
        boolean isonlog = false;
        for(Car c: cars){
          if((frog.left < c.right && frog.left > c.left)
          || (frog.right > c.left && frog.right < c.right)){
             isonlog = true;
          }
        }
        if(isonlog){
         frog.logMove(xspeed); 
        }else{
           ResetFrog(true); 
        }
       }else{
        //Car Logic
        for(Car c: cars){
          if((frog.left < c.right && frog.left > c.left)
          || (frog.right > c.left && frog.right < c.right)){
             ResetFrog(true);
          }
        }
      }
    }
  }
  
}
class RowController{
  ArrayList<Row> rows = new ArrayList();
  int hmr;
  int hme;
  boolean moving = false;
  int hasmoved = 0;
  RowController(boolean First){
    //How many active car rows have we made since the last empty row.
    hmr = 0;
    //How many empty rows have we made since the last car row.
    hme = 2;
    
   //Adjust the starting row number depending on if this the first level or not.
   int start;
    if(First){
      start = 2;
    }else{
      start = 33;
    }
    
    for(int i=start;i<start + 28;i++){
      if((i > 22 && First) || (i > 22+31 && !First)){
        //Log Row
           //Every row once we transition to the water area must be a log row.
        addRow(i,true);
      }else{
        //Car Row
          //Randomly either add a car row or dont with the following stipulations.
            //1. Always make the row directly before the first water row empty.
            //2. Never have more than 1 empty row in succession.
            //3. Never have more than 4 car rows in succession.
        if(i == 22 || i == 22+31){
          //Always one empty row before water section.
        }else if(hmr == 0 && hme == 2){
          addRow(i,false);
          hme = 0;
        }else if(hmr >=4){
          hmr = 0;
          hme = 1;
        }else if(hme >=1){
          addRow(i,false);
          hme = 0;
        }else if(hme > 0){
          if(random(1)>0.3f){
            addRow(i,false);
          }else{
           hme++; 
          }
        }else if(random(1)>0.5f){
            addRow(i,false);
        }else{
          hme++;
        }
      }
    }
    
    //Because the cars are randomly spawned in from the sides, 
      //we run the update function for a second to get the cars to actually be visible from the start.
    for(int i=0;i<1200;i++){
        update();
    }
  }
  
  
  public void addRow(int rownum, boolean islog){
    hmr++;
        float xspeed;
    if(islog){
    //Log Row
    xspeed = random(0.5f,1);
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.001f,0.002f),rownum,
    random(grid*1.5f,grid*3.5f),grid,xspeed,true));
    }else{
    //Car Row
    if(random(1)>0.8f){
      xspeed = random(0.75f,1.5f);
    }else{
      xspeed = random(0.75f,2);
    }
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.01f,0.05f),rownum,
    random(grid,grid*2),grid,xspeed,false));
    }
  }
  
  
  public void update(){
    if(!moving){
      for(Row r: rows){
        r.update();
      }
    }else{
      for(Row r: rows){
        r.moveDown();
      }
      hasmoved += 5;
    }
  }
  
  //Called everytime the frog moves rows so the row knows if it needs to run calcualtions or not.
  public void setfrogRow(){
    for(Row r: rows){
      if(r.rowNum == frogRow){
        r.froginrow = true;
      }else{
        r.froginrow = false;
      }
    }
  }
  
  public void show(){
      for(Row r: rows){
        r.show();
      }
  }
  public void moveDown(){
    if(!moving){
      moving = true;
    }
  }
  
  public void stopMove(){
    if(moving){
      moving = false;
      hasmoved = 0;
      
      //Sets the rows of the newly created level down to start back at 1.
      for(Row r: rows){
        r.rowNum = r.rowNum - 31;
      }
    }
  }
}
  public void settings() {  size(575,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Frogger" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
