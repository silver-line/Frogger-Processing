class Finish{
  
  Finish(){
    
  }
  void show(){
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