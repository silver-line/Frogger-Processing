class Finish{
  
  Finish(){
    
  }
  
  //Draw Checkerboard pattern
  void show(){
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