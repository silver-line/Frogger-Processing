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

public void setup(){
  
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

public void draw(){
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


public void keyPressed(){
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

public void ResetFrog(boolean died){
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
class Car extends Rectangle{
  float xspeed;
  int r;
  int g;
  int b;
  
  Car(float x,float y,float w, float h,float xspeed,int r,int g, int b){
    super(x,y,w,h);
    this.xspeed = xspeed;
    this.r = 255-r;
    this.g = 255-g;
    this.b = 255-b;
  }
  public void show(){
    fill(r,g,b);
    rectMode(CORNERS);
    //line(this.right,0,this.right,height);
    //line(this.left,0,this.left,height);
    rect(this.left,this.bottom - 3,this.right,this.top + 3);
    if(xspeed>0){
    //Right Side
       line(right-10,top + 3,right-10,bottom-3);
    }else{
    //Left Side
      line(left+10,top+3,left+10,bottom-3);
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
  public void show(){
    stroke(1);
    fill(255);
    for(int i=0;i<23;i++){
      if(i % 2 == 0){
        rect(i*grid,0,i*grid + grid,grid);
      }
    }
    for(int i=0;i<23;i++){
      if(i % 2 == 1){
        rect(i*grid,grid*2,i*grid + grid,grid);
      }
    }
    for(int i=0;i<23;i++){
      if(i % 2 == 0){
        rect(i*grid,grid*3,i*grid + grid,grid*2);
      }
    }
  }
}
class Frog extends Rectangle{
  
  Frog(float x,float y,float w){
    super(x,y,w,w);
  }
  
  public void show(){
    fill(255);
    rectMode(CORNERS);
    //line(this.right,0,this.right,height);
    //line(this.left,0,this.left,height);
    image(frogpic,this.left,this.top);
  }
  
  public void move(float x, float y){
   cpos(x*grid,y*grid); 
   this.offMap();
  }
  
  public void moveDown(){
    this.cpos(0,-5);
  }
  
  public void logMove(float move){
    this.cpos(move,0);
    this.offMap();
  }
}
class Log extends Car{

  Log(float x,float y,float w, float h,float xspeed,float rgb){
    super(x,y,w,h,xspeed,0,0,0);
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
  
  public boolean isOffMap(){
    if(left>=width){
     return true;
    }else if(right<=0){
      return true;
    }else{
     return false; 
    }
  }
  
  public void offMap(){
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
  
  public void overlap(){
    
  }
}
class Row{
  float chance;
  float cChance;
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
  this.cChance = chance;
  this.rowNum = PApplet.parseInt(y);
  this.y =height - (y * grid);
  this.xspeed = xspeed;
  this.w = w;
  this.h = h;
  if(logs){
    this.cooldown = (w*5*map(abs(xspeed),0.75f,3,0.75f,1));
  }else{
    this.cooldown = 10 + grid*3;
  }
  this.logs = logs;
  ccd = this.cooldown;
  if(logs){
    float rgb = random(1);
    r= PApplet.parseInt(map(rgb,0,1,28,48));
    g= PApplet.parseInt(map(rgb,0,1,60,103));
    b= PApplet.parseInt(map(rgb,0,1,144,255));
  }else{
    r= PApplet.parseInt(random(0,255));
    g= PApplet.parseInt(random(0,255));
    b= PApplet.parseInt(random(0,255));
  }
  }
  public void addCar(){
    if(xspeed > 0){
      if(logs){
        cars.add(0,new Log(0-w,y,w,h,xspeed,random(1)));
      }else{
        cars.add(0,new Car(0-w,y,w,h,xspeed,r,g,b));
      }
    }else{
      if(logs){
        cars.add(0,new Log(width,y,w,h,xspeed,random(1)));
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
  
  public void testAddNew(){
    if(ccd > cooldown){
      if(random(1)<chance){
        addCar();
        ccd = 0;
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
    hmr = 0;
    hme = 2;
   int start;
    if(First){
      start = 2;
    }else{
      start = 33;
    }
    for(int i=start;i<start + 28;i++){
      if((i > 22 && First) || (i > 22+31 && !First)){
        //Log Row
        addRow(i,true);
      }else{
        //Car Row
        if(i == 22 || i == 22+31){
          //Always one empty row before water section.
        }else if(hmr == 0 && hme == 2){
          addRow(i,false);
          hme = 0;
        }else if(hmr >3){
          hmr = 0;
          hme = 1;
        }else if(hme >1){
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
  }
  public void addRow(int rownum, boolean islog){
    hmr++;
    if(islog){
    //Log Row
    float xspeed = random(0.5f,1);
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.001f,0.002f),rownum,
    random(grid*1.5f,grid*3.5f),grid,xspeed,true));
    }else{
    //Car Row
    float xspeed;
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
  
  public void setFrogRow(){
    for(Row r: rows){
      if(r.rowNum == frogrow){
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
