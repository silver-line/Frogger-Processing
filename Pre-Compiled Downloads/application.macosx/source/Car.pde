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
  
  
  void show(){
    fill(r,g,b);
    rectMode(CORNERS);
    if(xspeed>0){
    //Car is drving Right (Display front of truck on right)
       rect(this.left,this.bottom - 3,this.right-7,this.top + 3);
       fill(r+10,g+10,b+10);
       rect(this.right-7,this.bottom - 3.5,this.right,this.top + 4.5);
    }else{
    //Car is drving Left (Display front of truck on left)
       rect(this.left+7,this.bottom - 3,this.right,this.top + 3);
       fill(r+10,g+10,b+10);
       rect(this.left,this.bottom - 3.5,this.left+7,this.top + 4.5);
    }
  }
  
  void update(){
    this.cpos(xspeed,0);
  }
  
  void moveDown(){
    this.cpos(0,-5);
  }
}