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
  void show(){
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
  
  void update(){
    this.cpos(xspeed,0);
  }
  
  void moveDown(){
    this.cpos(0,-5);
  }
}