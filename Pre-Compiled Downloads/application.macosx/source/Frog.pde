class Frog extends Rectangle{
  
  Frog(float x,float y,float w){
    super(x,y,w,w);
  }
  
  void show(){
    image(frogpic,this.left,this.top);
  }
  
  void move(float x, float y){
   cpos(x*grid,y*grid); 
   this.keepOnMap();
  }
  
  void moveDown(){
    this.cpos(0,-5);
  }
  
  //Helper function to let the log row move the frog with it confined to the screen.
  void logMove(float move){
    this.cpos(move,0);
    this.keepOnMap();
  }
}