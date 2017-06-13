//Basicly just a car with diffrent shading
//All Car or Log Logic is done via the containing Row.
class Log extends Car{

  Log(float x,float y,float w, float h,float xspeed){
    super(x,y,w,h,xspeed,0,0,0);
    
    //Set the logs color to a random brown ish color.
    float rgb = random(1);
    this.r = int(map(rgb,0,1,75,183));
    this.g = int(map(rgb,0,1,54,133));
    this.b = int(map(rgb,0,1,9,26));
  }
  
  void show(){
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