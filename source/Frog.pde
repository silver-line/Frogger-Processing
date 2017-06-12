class Frog extends Rectangle{
  
  Frog(float x,float y,float w){
    super(x,y,w,w);
  }
  
  void show(){
    fill(255);
    rectMode(CORNERS);
    //line(this.right,0,this.right,height);
    //line(this.left,0,this.left,height);
    image(frogpic,this.left,this.top);
  }
  
  void move(float x, float y){
   cpos(x*grid,y*grid); 
   this.offMap();
  }
  
  void moveDown(){
    this.cpos(0,-5);
  }
  
  void logMove(float move){
    this.cpos(move,0);
    this.offMap();
  }
}