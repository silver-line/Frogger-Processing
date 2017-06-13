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
          if(random(1)>0.3){
            addRow(i,false);
          }else{
           hme++; 
          }
        }else if(random(1)>0.5){
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
  
  
  void addRow(int rownum, boolean islog){
    hmr++;
        float xspeed;
    if(islog){
    //Log Row
    xspeed = random(0.5,1);
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.001,0.002),rownum,
    random(grid*1.5,grid*3.5),grid,xspeed,true));
    }else{
    //Car Row
    if(random(1)>0.8){
      xspeed = random(0.75,1.5);
    }else{
      xspeed = random(0.75,2);
    }
    if(rownum % 2 == 0){
      xspeed = xspeed *-1;
    }
    rows.add(new Row(random(0.01,0.05),rownum,
    random(grid,grid*2),grid,xspeed,false));
    }
  }
  
  
  void update(){
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
  void setfrogRow(){
    for(Row r: rows){
      if(r.rowNum == frogRow){
        r.froginrow = true;
      }else{
        r.froginrow = false;
      }
    }
  }
  
  void show(){
      for(Row r: rows){
        r.show();
      }
  }
  void moveDown(){
    if(!moving){
      moving = true;
    }
  }
  
  void stopMove(){
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