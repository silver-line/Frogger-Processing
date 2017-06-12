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
  
  void cpos(float x, float y){
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
  
  boolean isOffMap(){
    if(left>=width){
     return true;
    }else if(right<=0){
      return true;
    }else{
     return false; 
    }
  }
  
  void offMap(){
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
  
  void overlap(){
    
  }
}